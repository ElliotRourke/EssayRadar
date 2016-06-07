package main;

/**
 * This is the Report class.
 * The report class is responsible for defining what must be included in a report.
 * It sets and gets the values used in a report.
 */
public class Report {

	private double score;
	private String filename;
	private String grade;
	private int id;

	public Report(int id,String filename, double score, String grade){
		setId(id);
		setFilename(filename);
		setScore(score);
		setGrade(grade);
	}

	public Report(){

	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getScore() {
		return score;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Method used for unit testing.
	 */
	@Override
	public String toString() {
		return new String(getFilename() + " : " + getScore());
	}

}
