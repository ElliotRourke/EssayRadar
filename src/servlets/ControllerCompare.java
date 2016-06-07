package servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import main.Upload;

/**
 * This is the ControllerCompare class.
 * The class is responsible for handling the upload from the user and sending the data to the Upload class.
 * The class retrieves the file name and saves the file in the applications relative path.
 * "C:\..Installation path\Radar 2.0\WebContent\WEB-INF\data\..."
 * @see Upload
 */
@WebServlet("/compare")
@MultipartConfig(fileSizeThreshold=1024*1024,maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)

public class ControllerCompare extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	/**
	 * This method receives the file.
	 * It finds the name, handles the data and first level exceptions.
	 * The file is then passed to the Upload class for parsing.
	 * @see Upload
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			File uploads = new File("F:/Eclipse/Radar 2.0/WebContent/WEB-INF/data/");
			Part filePart = request.getPart("file"); 
			String fileName = getOriginalFileName(filePart);
			File fName = new File(fileName);
			String destination = (uploads + File.separator + fName.getName());
	
	
			if(fileName.equals("")){
				request.setAttribute("upmessage", "true");
				request.getServletContext().getRequestDispatcher("/WEB-INF/view/upload.jsp").forward(request,response);
			}else{
					try{
		
						File file = new File(uploads, fName.getName());
						try (InputStream fileContent = filePart.getInputStream()) {
							Files.copy(fileContent, file.toPath());
						}
						Upload up = new Upload();
						up.uploadFiles(destination);

						response.sendRedirect("result");
						return;
		
					}catch(FileAlreadyExistsException e){
						request.setAttribute("upmessage", "true");
						request.getServletContext().getRequestDispatcher("/WEB-INF/view/upload.jsp").forward(request,response);
					}catch(AccessDeniedException e){
						request.setAttribute("upmessage", "true");
						request.getServletContext().getRequestDispatcher("/WEB-INF/view/upload.jsp").forward(request,response);
					}catch(ServletException e){
						request.setAttribute("upmessage", "true");
						request.getServletContext().getRequestDispatcher("/WEB-INF/view/upload.jsp").forward(request,response);
					}
				}
			}catch(IllegalStateException e){
				request.setAttribute("upmessage", "true");
				request.getServletContext().getRequestDispatcher("/WEB-INF/view/upload.jsp").forward(request,response);
			}
	}

	/**
	 * This method takes the file part and extracts the name from the header,
	 * it replaces any backslahes found with spaces and then splits the path removing thing final piece - the name.
	 */
	public static String getOriginalFileName(Part part)
	{
		String contentDisposition = part.getHeader("content-disposition");
		if (contentDisposition != null){

			for (String cd : contentDisposition.split(";")){

				if (cd.trim().startsWith("filename")){
					return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");	
				}
			}
		}

		return null;
	}

}
