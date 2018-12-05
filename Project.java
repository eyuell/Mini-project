package ProjectScheduling;

import java.time.LocalDate;
import java.util.ArrayList;

public class Project {
    private String name;
    private String projectID;
    private ArrayList<Task> tasks;
    //private ArrayList<Finance> finances;
    //private ArrayList<Risk> risks;
    private long duration;
    private LocalDate startDate;
    private LocalDate finishDate;
    private double cost;

    public Project (String name, String projectID, LocalDate startDate){
        this.name = name;
        this.projectID = projectID;
        this.startDate = startDate;
        this.tasks = new ArrayList<>();
        //this.finances = new ArrayList<>();
        //this.risks = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public String getProjectID() {
        return projectID;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDate getFinishDate() { return finishDate; }

    public void addTask(Task task){
        this.tasks.add(task);
    }

    //public void addFinance(Finance finance){this.finances.add(finance);}

    //public void addRisk(Risk risk){this.risks.add(risk);}

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public LocalDate getStartDate() { return startDate; }

    public double getCost() {
        return cost;
    }

    //public ArrayList<Finance> getFinances() { return finances; }

    //public ArrayList<Risk> getRisks() { return risks; }

    public void setName(String name) {
        this.name = name;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public void setFinishDate(LocalDate finishDate) { this.finishDate = finishDate; }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}