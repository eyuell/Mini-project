﻿package MiniProject;

import com.google.gson.Gson; //to convert from object to json
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.print.Printer;
//import com.sun.xml.internal.bind.v2.model.core.ID;

import javax.naming.Name;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

//the project system Main class
public class ProjectManagementTool {

    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String RED = "\033[0;31m";     // RED
    public static final String BLUE = "\033[0;34m";    // BLUE

    // Background
    public static final String RED_BACKGROUND = "\033[41m";    // RED
    public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

    //the system has projects but we work on one project.
    // only for json we use the object
    private ArrayList<Project> projects;

    //constructor
    public ProjectManagementTool(){

        this.projects = new ArrayList<>();
    }

    //getter for the projects
    public ArrayList<Project> getProjects() {
        return projects;
    }

    //Gateway to the system
    public static void main(String [] args) throws Exception {

        System.out.println("This program works on project schedules");
        ProjectManagementTool start = new ProjectManagementTool();
        start.run();
    }

    //Main Menu of the program
    public void printMenuOption(){
        System.out.println("=========================================");
        System.out.println("1. Register Project");
        System.out.println("2. Register Tasks and Milestones");
        System.out.println("3. Register Team members");
        System.out.println("4. Assign time to Tasks");
        System.out.println("5. Assign Manpower to Tasks");
        System.out.println("6. Register Actual Resources to Tasks");
        System.out.println("7. Display All projects:");
        System.out.println("8. Display Tasks and Milestones");
        System.out.println("9. Display Team Members");
        System.out.println("10. Display Project Schedule");
        System.out.println("11. Monitor Finances");
        System.out.println("12. Monitor Time Spent");
        System.out.println("13. Monitor Participation");
        System.out.println("14. Monitor Risk");
        System.out.println("15. Edit Information");
        System.out.println("16. QUIT Program");
        System.out.println("=========================================");
        System.out.println();
    }
    
    //Second switch for option16 in menu
    public void printMenuOptions(){
        System.out.println("=========================================");
        System.out.println("1. Edit Tasks");
        System.out.println("2. Edit Project duration");
        System.out.println("3. Edit Current team Member information");
        System.out.println("4. Edit Project");
        System.out.println("5. Return");
        System.out.println("=========================================");
        System.out.println();
    }//Armin


    //the gate way to the menu
    public void run() throws Exception {
        int optionChoice;
        final int REGISTER_PROJECT = 1;
        final int REGISTER_TASKS_MILESTONES = 2;
        final int REGISTER_TEAM_MEMBERS = 3;
        final int ASSIGN_TIME = 4;
        final int ASSIGN_MANPOWER = 5;
        final int REGISTER_ACTUAL_DATA = 6;
        final int PRINT_ALL_PROJECTS = 7;
        final int PRINT_TASKS = 8;
        final int PRINT_TEAM_MEMBERS = 9;
        final int PRINT_PLANED_ACTUAL_SCHEDULE = 10;
        final int MONITOR_FINANCES = 11;
        final int MONITOR_TIME_SPENT = 12;
        final int MONITOR_PARTICIPATION = 13;
        final int MONITOR_RISK = 14;
        final int EDIT_INFO = 15;
        final int QUIT = 16;

        readFromSystemClass(); //to read data by initiating a project with set values in the internal system
        //readFromJsonFile(); //to read data from stored json file

        //checking all tasks are complete with major timeline info
        projectCompletenessCheck();

        //menu
        do {
            printMenuOption();
            System.out.print("Choose menu option ");
            optionChoice = new KeyboardInput().Int();     //keyboard input for the project

            switch (optionChoice){
                case REGISTER_PROJECT:
                    registerProject();
                    break;

                case REGISTER_TASKS_MILESTONES:
                    registerTasksAndMilestones();
                    break;

                case REGISTER_TEAM_MEMBERS:
                    registerTeamMember();
                    break;

                case ASSIGN_TIME:
                    assignTime();
                    break;

                case ASSIGN_MANPOWER:
                    assignManPower();
                    break;

                case REGISTER_ACTUAL_DATA:
                    registerActualData();
                    break;

                case PRINT_ALL_PROJECTS:
                    printProjects();
                    break;

                case PRINT_TASKS:
                    printTasks();
                    break;

                case PRINT_TEAM_MEMBERS:
                    printTeamMembers();
                    break;

                case PRINT_PLANED_ACTUAL_SCHEDULE:
                    printPlannedAndActualSchedule();
                    break;

                case MONITOR_FINANCES:
                    monitorCosts();
                    break;

                case MONITOR_TIME_SPENT:
                    monitorTimeSpent();
                    break;

                case MONITOR_PARTICIPATION:
                    monitorParticipation();
                    break;

                case MONITOR_RISK:
                    monitorRisk();
                    break;

                case EDIT_INFO:
                    editInfo();
                    break;

                case QUIT:
                    System.out.println("Saving updates. Do not turn off the computer! ");
                    System.out.println("***");
                    break;

                default:
                    System.out.println("Option " + optionChoice + " is not valid.");
                    System.out.println("***");
                    break;
            }
        } while (optionChoice != QUIT);

        writeProjectToJsonFile(); //Writes to project data store in Json file
    }

    //to register projects with error handling
    public void registerProject(){
        System.out.println();
        System.out.print("Enter name of a project ");
        String projectName = new KeyboardInput().Line();
        
        while (checkProjectName(projectName)){
                System.out.print("Project Name is already used. Enter another name : ");
                projectName = new KeyboardInput().Line();
            }

        System.out.println();
        String projectID;
        do {
            System.out.print("Enter project ID number ");
            projectID = new KeyboardInput().Line();
            if (projectExists(projectID)) {
                System.out.println("A project with this id already exists");
            }
        } while (projectExists((projectID)));

        int choice;
        final int SECOND_OPTION = 2;
        System.out.println("How do you set the project Start and End dates :");
        do{
            System.out.println("    1. Set the dates now");
            System.out.println("    2. Get the dates from the tasks ");
            System.out.print("Enter option 1 or 2 ");
            choice = new KeyboardInput().positiveInt();
            if(choice > SECOND_OPTION){
                System.out.println("Incorrect option choice ");
            }
        } while (choice > SECOND_OPTION);

        LocalDate projectStartDate = null;
        LocalDate projectFinishDate = null;
        long duration = 0;

        if(choice != SECOND_OPTION){ //dates now
            System.out.println("Enter the Start date of the project: ");
            projectStartDate = new DataEvaluator().readDate();

            System.out.println("Enter the Finish date of the project: ");
            projectFinishDate = new DataEvaluator().readDate();

            duration = ChronoUnit.DAYS.between(projectStartDate, projectFinishDate) + 1;
        }

        Project project = new Project(projectName, projectID, projectStartDate);
        projects.add(project);
        project.setFinishDate(projectFinishDate);
        project.setDuration(duration);
        System.out.println();
        System.out.println("Project '" + projectName + "' is registered successfully !");
        pause();
    }

    //method to register tasks and Milestones with error handling
    public void registerTasksAndMilestones(){

        Project foundProject = projects.get(FIRST);
        String taskID;
        String milestoneID;

        if(foundProject != null){
            boolean continueAdding = true;

            while(continueAdding){
                System.out.println();
                System.out.print("Enter name of a new task (Enter * to stop adding and exit) ");
                String taskName = new KeyboardInput().Line();
                taskName = new DataEvaluator().nameLength(taskName);
                if(taskName.equals("*")){
                    continueAdding = false;
                }

                if(continueAdding){
                    taskID = readNewTaskID(foundProject);

                    int typeOfTask = typeOfTask();

                    Task newTask = new Task(taskName, taskID, typeOfTask);
                    System.out.println();
                    System.out.println("Task '" + taskName + "' (" + taskID + ") is registered successfully... ");
                    foundProject.getTasks().add(newTask);
                }
            }
            
            final String YES = "Y";
            //if there is a choice to register milestones now
            System.out.print("Do you want to add Milestones to the project ? (Y/N) ");
            String choice = new KeyboardInput().Line();
            choice = new DataEvaluator().yesOrNo(choice);

            if(choice.equals(YES)){
                continueAdding = true;

                while(continueAdding){
                    System.out.println();
                    System.out.print("Enter name of a new Milestone (Enter * to stop adding and exit) ");
                    String milestoneName = new KeyboardInput().Line();
                    milestoneName = new DataEvaluator().nameLength(milestoneName);

                    if(milestoneName.equals("*")){
                        continueAdding = false;
                    }

                    if(continueAdding){
                        milestoneID = readNewMilestoneID(foundProject);
                        System.out.println("Enter the date of the Milestone ");
                        LocalDate date = new DataEvaluator().readDate();

                        Milestone newMilestone = new Milestone(milestoneName, milestoneID, date);
                        System.out.println();
                        System.out.println("Milestone '" + milestoneName + "' (" + milestoneID + ") is registered successfully... ");
                        foundProject.getMilestones().add(newMilestone);
                    }
                }
            }
        }else{
            System.out.println("A project does not exist to add tasks on ");
        }
        pause();
    }

    //register team members with error handeling
    public void registerTeamMember(){

        if(projects != null){
            Project foundProject = projects.get(FIRST);
            System.out.print("Enter the name of Team Member ");
            String name = new KeyboardInput().Line();

            while(teamMemberExists(foundProject, name)){
                System.out.println("The name " + name + " is already registered ");
                System.out.println();
                System.out.print("Do you want to continue registering by re-writing name details ? (Y/N) ");
                String choice = new KeyboardInput().Line();
                choice = new DataEvaluator().yesOrNo(choice);
                if(choice.equals("Y")){
                    System.out.print("Enter the name of Team Member ");
                    name = new KeyboardInput().Line();
                }else{
                    name = ""; // to exit the loop and not add it later.
                }
            }

            if(! name.equals("")){
                System.out.print("Enter ID number for new team member " + name + " ");
                String id = new KeyboardInput().Line();

                while (retrieveTeamMember(foundProject, id) != null){
                    System.out.println("The id " + id + " is already used by other team member.");
                    System.out.println();
                    System.out.print("Enter a new ID number ");
                    id = new KeyboardInput().Line();
                }

                String qualification = readQualification();

                foundProject.getTeamMembers().add(new TeamMember(name, id, qualification));
            }

        } else {
            System.out.println("The project does not exist");
        }
        pause();
    }

    //assign start, finish and duration of tasks
    public void assignTime(){
        int STAND_ALONE = 1;
        int DEPENDANT = 2;
        if(projects != null){
            Project foundProject = projects.get(FIRST);
            ArrayList<Task> allTasks = foundProject.getTasks();

            if (allTasks != null){
                LocalDate taskStartDate;
                LocalDate taskFinishDate;
                long taskPlannedDuration;
                int numberOfTasks = allTasks.size();

                for (int i = 0 ; i < numberOfTasks; i++){
                    Task currentTask = allTasks.get(i);
                    boolean completenessCheck = completenessCheck(currentTask);
                    if(! completenessCheck){ //if not complete
                        int taskType = currentTask.getTypeOfTask();

                        System.out.println();
                        System.out.println("Data for task '" + currentTask.getName() + "' : ");
                        if(taskType == STAND_ALONE){ //stand alone
                            System.out.println("Enter the start date of the task ");
                            taskStartDate = new DataEvaluator().readDate();
                            currentTask.setPlannedStart(taskStartDate);

                            int BY_FINISH_DATE = 2;
                            int lengthOfTask = readLengthOfTask();

                            if(lengthOfTask == STAND_ALONE){
                                System.out.print("Enter the planned duration of the task ");
                                taskPlannedDuration = new KeyboardInput().positiveInt();
                                currentTask.setPlannedDuration(taskPlannedDuration);
                                LocalDate finishDate = currentTask.getPlannedStart().plusDays(taskPlannedDuration);
                                currentTask.setPlannedFinish(finishDate);
                            }else if(lengthOfTask == BY_FINISH_DATE){
                                System.out.println("Enter the finish date of the task ");
                                taskFinishDate = new DataEvaluator().readDate();
                                currentTask.setPlannedFinish(taskFinishDate);

                                long duration = ChronoUnit.DAYS.between(currentTask.getPlannedStart(), taskFinishDate) + 1;

                                currentTask.setPlannedDuration(duration);
                            }
                        } else if (taskType == DEPENDANT){ //dependant on other task
                            String connectivityType;
                            long connectivityDuration = 0;
                            boolean repeat;

                            do{
                                repeat = false;
                                Task foundTask;
                                System.out.println("How many connectivity does this task has with other tasks ?");
                                int numberOfConnectivity = new KeyboardInput().positiveInt();
                                for(int j = 0; j < numberOfConnectivity; j++){
                                    System.out.println("Connectivity " + (j + 1) + ": ");
                                    System.out.println("On which task does the current task depend on ? ");
                                    String toBeConnectedToTaskID = readExistingTaskID(foundProject);
                                    foundTask = retrieveTask(toBeConnectedToTaskID, foundProject);

                                    connectivityType = readConnectivityType();

                                    System.out.print("Enter the duration of connectivity. (It could be negative if applicable) ");
                                    connectivityDuration = new KeyboardInput().Int();

                                    currentTask.getConnectivity().add(new Connectivity(foundTask, connectivityType, connectivityDuration));
                                }

                                LocalDate startDate = new DataEvaluator().extractConnectivityDate("start",currentTask.getConnectivity() );
                                LocalDate finishDate = new DataEvaluator().extractConnectivityDate("finish",currentTask.getConnectivity() );

                                if(startDate != null &&  finishDate != null){
                                    if(finishDate.isAfter(startDate)){

                                        long duration = ChronoUnit.DAYS.between(startDate, finishDate) +1;

                                        currentTask.setPlannedStart(startDate);
                                        currentTask.setPlannedFinish(finishDate);
                                        currentTask.setPlannedDuration(duration);
                                        repeat = false;
                                    }else {
                                        System.out.println("There is an error on connectivity that results an end date before a start date. Correct the data again");
                                        repeat = true;
                                    }
                                }else {
                                    System.out.print("Enter the planned duration of the task ");
                                    taskPlannedDuration = new KeyboardInput().positiveInt();
                                    currentTask.setPlannedDuration(taskPlannedDuration);
                                    if(startDate != null){
                                        finishDate = startDate.plusDays(taskPlannedDuration);
                                        currentTask.setPlannedFinish(finishDate);
                                    } else {
                                        startDate = finishDate.minusDays(taskPlannedDuration);
                                        currentTask.setPlannedStart(startDate);
                                    }
                                }
                            }while (repeat);
                        }
                        //completeness once again
                        completenessCheck(currentTask);
                    }
                }
                checkWithProjectTimes(foundProject);
                System.out.println("All tasks planned times are complete ");
            }else {
                System.out.println("There are no tasks in the project yet ");
            }
        } else {
            System.out.println("The project does not exist");
        }
        pause();
    }

    public void assignManPower(){

        if (projects != null){
            Project foundProject = projects.get(FIRST);
            if(foundProject.getTasks() != null){
                int numberOfTasks = foundProject.getTasks().size();
                for(int i = 0; i < numberOfTasks; i++){
                    System.out.println();
                    Task task = foundProject.getTasks().get(i);
                    LocalDate startDate = task.getPlannedStart();
                    long days = task.getPlannedDuration();
                    for(int j = 0; j < days; j++){
                        boolean repeat;
                        LocalDate date = startDate.plusDays((long) j); //date
                        do{
                            repeat = true;
                            System.out.println("Enter Man hour need for Task ID (" + task.getId() + ") on date " + date + ": ");
                            System.out.println("    Enter * to escape to another day ");
                            String timeInput = new KeyboardInput().Line();
                            if(timeInput.equals("*")){
                                completenessCheck(task); // this may not be needed
                                repeat = false;
                            }else{
                                double time = new DataEvaluator().positiveDouble(timeInput); //time
                                String qualification = readQualification(); //qualification

                                Manpower manPower = new Manpower(qualification);
                                task.getPlannedManpower().add(new ManpowerAllocation(manPower,time,date));
                            }
                        }while (repeat);
                    }
                }
                System.out.println();
                System.out.println("Manpower allocated successfully...");
            }else{
                System.out.println("There are no tasks in the project");
            }
        }else {
            System.out.println("There are no projects to show");
        }
        pause();
    }

    public void registerActualData(){
        LocalDate today = LocalDate.now();

        if (projects != null ){
            Project foundProject = projects.get(FIRST);
            if(foundProject.getTasks() != null){
                String taskId = readExistingTaskID(foundProject);
                Task foundTask = retrieveTask(taskId,foundProject);
                if(foundTask.getActualStart() == null){ //if no start date
                    System.out.println("Actual start date of the task " + taskId + " needs to be provided first. ");
                    LocalDate startDate = new DataEvaluator().readDate();

                    while(startDate.isAfter(today)){
                        System.out.print("An actual start date cannot be after today. Enter a correct date ");
                        startDate = new DataEvaluator().readDate();
                    }
                    foundTask.setActualStart(startDate);
                }

                LocalDate possibleFinalDate = today;

                if(foundTask.getActualStart() != null){
                    if(foundTask.getActualFinish() == null){ //if no finish
                        boolean taskStatus = readActualTaskStatus();
                        foundTask.setStatusOfTask(taskStatus);
                        if(taskStatus){
                            System.out.println("Enter the actual finish date of the task ");
                            boolean beforeStart;
                            do{
                                possibleFinalDate = new DataEvaluator().readDate();
                                beforeStart = possibleFinalDate.isBefore(foundTask.getActualStart());

                                if(beforeStart){
                                    System.out.print("Actual finish date cannot be before actual start date. Enter the correct finish date ");
                                }
                            }while (beforeStart);
                        }
                        foundTask.setActualFinish(possibleFinalDate);
                        //
                    }else if(foundTask.getActualFinish().equals(today)){ // if the task is active
                        System.out.print("Do you want to update the actual finish date ? (Y/N) ");
                        String choice = new KeyboardInput().Line();
                        choice = new DataEvaluator().yesOrNo(choice);
                        if(choice.equals("Y")){ //TBD: here may be i need to check if already entered resources data is not out of final day
                            System.out.println("Enter the actual finish date ");
                            boolean beforeStart;
                            LocalDate finish;
                            do{
                                finish = new DataEvaluator().readDate();
                                beforeStart = finish.isBefore(foundTask.getActualStart());

                                if(beforeStart){
                                    System.out.print("Actual finish date cannot be before actual start date. Enter the correct finish date ");
                                }
                            }while (beforeStart);

                            foundTask.setActualFinish(finish);
                            foundTask.setStatusOfTask(true);
                        }
                    }

                    System.out.print("Do you want to register actual data for this task ? (Y/N) ");
                    String yesOrNo = new KeyboardInput().Line();
                    yesOrNo = new DataEvaluator().yesOrNo(yesOrNo);
                    if(yesOrNo.equals("Y")){
                        System.out.println("On which day do you want to register data ? ");
                        LocalDate editDate = new DataEvaluator().readDate();

                        while(editDate.isAfter(foundTask.getActualFinish()) || editDate.isBefore(foundTask.getActualStart())){
                            System.out.print("Date should be between Actual Start date ");
                            if(foundTask.getActualFinish() != null){
                                System.out.print("and finish date.");
                            }else{
                                System.out.println("and today.");
                            }
                            System.out.print(" Enter a correct date ");
                            editDate = new DataEvaluator().readDate();  //date
                        }

                        System.out.println("Which team member has participated on the task ? ");
                        TeamMember foundMember;
                        do{
                            System.out.print("Enter id ");
                            String id = new KeyboardInput().Line();
                            foundMember = retrieveTeamMember(foundProject, id);
                            if(foundMember == null){
                                System.out.println("Team member not found. Try again ");
                            }
                        }while (foundMember == null);

                        System.out.print("How long time is to be registered in decimal form ");
                        double hoursWorked = new KeyboardInput().positiveDouble();  //hours
                        foundTask.getActualTeamMembers().add(new TeamMemberAllocation(foundMember,hoursWorked,editDate));

                        updateDates(foundTask, foundTask.getPlannedStart(),foundTask.getPlannedFinish(),foundTask.getActualStart(),foundTask.getActualFinish());
                    }
                }
                //
            }else{
                System.out.println("There are no tasks in the project ");
            }
        }else{
            System.out.println("There are no projects to show ");
        }
        pause();
    }


       public void printTasks(){
       System.out.println("What do you wish to edit?");
        System.out.println("1. All Tasks ");
        System.out.println("2. Specific Task");
    int option= new KeyboardInput().Int();
        if(option==1){
       printAllTasksAndMilestones();
    }
        else if(option==2){
        printSpecifitTaskMilestones();
    }
    }//Armin


public void printAllTasksAndMilestones(){
    Project currentProject = projects.get(FIRST);
    ArrayList<Task> tasks = currentProject.getTasks();

    for (int i = 0; i <tasks.size() ; i++) {
        System.out.println(tasks.get(i).getName());
    }

    System.out.println("Do you wish to print milestones?");
    System.out.println("1. Yes");
    System.out.println("2. No");

    int option = new KeyboardInput().Int();

    if (option == 1) {
    printMileStones();
    }

    else if (option == 2) {
        System.out.println("Milestones not printed.");
    }

}//Armin



    public void printSpecifitTaskMilestones() {//ONLY 1 TASK PRINT //Armin parial
        Project currentProject = projects.get(FIRST);
        System.out.println("Enter the id of a task");
        String taskID = new KeyboardInput().Line();

        do {
            if (retrieveTask(taskID, currentProject) == null) {
                System.out.print("Task does not exist or wrong ID. Enter correct ID again ");
                taskID = new KeyboardInput().Line();
            }
        }
        while (this.retrieveTask(taskID, currentProject) == null);

        Task foundTask = retrieveTask(taskID, currentProject);
        System.out.println(foundTask.getName());

        System.out.println("Do you wish to print milestones?");
        System.out.println("1. Yes");
        System.out.println("2. No");

        int option = new KeyboardInput().Int();
        if (option == 1) {
            printMileStones();
        }

        else if (option == 2) {
            System.out.println("Milestones not printed.");
        }
    }//Armin

public void printMileStones(){
    System.out.println("Do you wish to print: ");
    System.out.println("1. All Milestones");
    System.out.println("2. Specific Milestone");

    int option=new KeyboardInput().Int();

    if (option==1){
        printAllMilestones();
    }
    if (option==2){
        printSpecificMileStones();
    }
}//Armin


public void printSpecificMileStones(){
    Project currentProject = projects.get(FIRST);
    System.out.println("Enter the id of a Milestone you wish to print");
    String milestoneID = new KeyboardInput().Line();

    Milestone foundMilestone = retrieveMilestone(currentProject,milestoneID);
    System.out.println("Name: "+foundMilestone.getName());
    System.out.println("milestone date: " + foundMilestone.getDate());

}//Armin


public void printAllMilestones() {
    Project currentProject = projects.get(FIRST);
    ArrayList<Milestone> milestones = currentProject.getMilestones();
    if (milestones != null) {
        for (int i = 0; i < milestones.size(); i++) {
            System.out.println("Name: "+milestones.get(i).getName());
            System.out.println("Milestone Date: "+milestones.get(i).getDate());
            System.out.println("*************************************");
        }
    }
}//Armin


    public void printProjects() {//Armin
        int option;
        System.out.println("Do you wish to print: ");
        System.out.println("1. All projects");
        System.out.println("2. Specific Project");
        option= new KeyboardInput().Int();
        if (option==1){
            printAllProjects();
        }
        if (option==2){
            System.out.println("HAMID METHOD HERE!!!");
        }
        }//Armin

public void printAllProjects() {
        System.out.println("Here is a List of all Current projects: ");
        System.out.println("----------------------------------------------------------------");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println("Project ID Assigned: "+projects.get(i).getProjectID());
            System.out.println("Project name: "+projects.get(i).getName());
            System.out.println("Project Start date: "+projects.get(i).getStartDate());
            System.out.println("Project End date: "+projects.get(i).getFinishDate());
            System.out.println("----------------------------------------------------------------");

        }
    }



   public void printTeamMembers() { //Armin
        System.out.println("Do you wish to print a specific or All team members? ");
        System.out.println("1. All team members");
        System.out.println("2. Specific team member");
        System.out.println("---------------------------------");

        int option = new KeyboardInput().Int();
        if (option == 1) {
            printAllTeamMembers();
        } 
       else if (option == 2) {
            printSpecificTeamMember();
        }
    }//Armin


    public void printSpecificTeamMember() {//Armin
        Project currentProject = projects.get(FIRST);
        System.out.println("Enter the id of a team member: ");
        String teamID = new KeyboardInput().Line();
        System.out.println("----------------------------------------------------------------");

        while (!teamMemberIDExists(currentProject, teamID)) {
            System.out.print("Team member does not exist or wrong ID. Enter correct ID again ");
            teamID = new KeyboardInput().Line();
            System.out.println("----------------------------------------------------------------");

        }

        TeamMember foundTeamMember = retrieveTeamMember(currentProject, teamID);
        System.out.println("Name: "+foundTeamMember.getName());
        System.out.println("proffesion: "+foundTeamMember.getQualification());
        System.out.println("Hourly salary: "+foundTeamMember.getHourlyRate());
        System.out.println("----------------------------------------------------------------");

}//Armin


    public void printAllTeamMembers() {//Armin
        Project currentProject = projects.get(FIRST);
            System.out.println("Here is a list of all Team members currently registered: ");
            System.out.println("----------------------------------------------------------------");
            for (int i = 0; i < currentProject.getTeamMembers().size(); i++) {
                    System.out.println("Name: " + currentProject.getTeamMembers().get(i).getName());
                    System.out.println("Proffession: " + currentProject.getTeamMembers().get(i).getQualification());
                    System.out.println("----------------------------------------------------------------");
            }
    }//Armin


    public void printPlannedAndActualSchedule(){

        if (projects != null){
            Project foundProject = projects.get(FIRST);
            if(foundProject.getTasks() != null){
                ArrayList<Task> tasks = foundProject.getTasks();
                int taskNameLength = tasks.get(0).getName().length();
                int taskIdLength = tasks.get(0).getId().length();
                int smallestIndent = taskNameLength + taskIdLength;

                ArrayList<String> listOfNames = new ArrayList<>();
                for (int i = 1; i < tasks.size(); i++){
                    String indent = tasks.get(i).getName() + tasks.get(i).getId();
                    listOfNames.add(indent);
                }

                ArrayList<Milestone> milestones = foundProject.getMilestones();
                if(milestones != null){
                    for(int i = 0; i < milestones.size();i++){
                        String indent = milestones.get(i).getName() + milestones.get(i).getId();
                        listOfNames.add(indent);
                    }
                }

                for (int i = 0; i < listOfNames.size(); i++) {
                    int indent = listOfNames.get(i).length();
                    if(indent > smallestIndent){
                        smallestIndent = indent;
                    }
                }

                if( smallestIndent < 20){
                    smallestIndent = 20;
                }

                System.out.println(); //blank line
                LocalDate localDate = LocalDate.now();
                LocalDate tasksStartDate = tasksStartAndFinishDates("start",tasks);
                LocalDate tasksFinishDate = tasksStartAndFinishDates("finish",tasks);

                long duration = ChronoUnit.DAYS.between(tasksStartDate, tasksFinishDate) + 1;
                //
                for(int i = 0;i < (smallestIndent + 5); i++){//horizontal line1
                    System.out.print("_");
                }
                for (long i = 0; i < duration; i++){//horizontal line2
                    System.out.print("____________");
                }
                System.out.println();
                printEmpty(smallestIndent);
                System.out.println("        " + foundProject.getName() + " (" + foundProject.getProjectID() + ")");
                printEmpty(smallestIndent);
                System.out.println("        Date: " + localDate);
                System.out.println();
                System.out.println();
                int beforeText = smallestIndent/4;
                printEmpty(beforeText);
                System.out.print("Tasks/Milestones");
                int afterText = smallestIndent-beforeText - 16 + 4;
                printEmpty(afterText);
                System.out.print("|");
                for (long i = 0; i < duration; i++){//the days printed
                    LocalDate day = tasksStartDate.plusDays(i);
                    System.out.print("|" + day + "|");//12 pixels per day ?
                }
                System.out.println();
                for(int i= 0;i<(smallestIndent + 5);i++){//horizontal line1
                    System.out.print("_");
                }
                for (long i = 0; i < duration; i++){//horizontal line2
                    System.out.print("____________");
                }
                System.out.println();
                System.out.println();
                System.out.println();

                for(int i = 0; i < tasks.size();i++){
                    Task currentTask = tasks.get(i);
                    int taskIndent = smallestIndent - currentTask.getName().length() - currentTask.getId().length() + 4 - 2;

                    //planned plot
                    System.out.print(currentTask.getName()+"(" + currentTask.getId()+")");
                    printEmpty(taskIndent);
                    System.out.print("|");
                    boolean print;
                    for (long j = 0; j < duration; j++){// project tasks duration
                        LocalDate day = tasksStartDate.plusDays(j);
                        print = true;
                        for(long m = 0; m < currentTask.getPlannedDuration(); m++){
                            LocalDate taskDates = currentTask.getPlannedStart().plusDays(m);
                            if(day.equals(taskDates)){
                                System.out.print(BLUE + "|==========|" + RESET);//12 pixels per day ?
                                print = false;
                            }
                        }
                        if(print){
                            System.out.print("     .      ");//12 pixels per day ?
                        }
                    }
                    System.out.println();

                    //Actual plot
                    if(currentTask.getActualStart() != null){
                        printEmpty(smallestIndent + 4);
                        System.out.print("|");
                        for (long j = 0; j < duration; j++){// project tasks duration
                            LocalDate day = tasksStartDate.plusDays(j);

                            LocalDate thisStart = currentTask.getActualStart();
                            LocalDate thisFinish = currentTask.getActualFinish();

                            long actualDuration = ChronoUnit.DAYS.between(thisStart, thisFinish) + 1;

                            print = true;
                            for(int m = 0; m < actualDuration; m++){
                                LocalDate taskDates = thisStart.plusDays(m);
                                if(day.equals(taskDates)){
                                    System.out.print(RED + "|**********|" + RESET);//12 pixels per day ?
                                    print = false;
                                }
                            }
                            if(print){
                                System.out.print("     .      ");//12 pixels per day ?
                            }
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
                //Milestones
                ArrayList<Milestone> milestones1 = foundProject.getMilestones();
                if(milestones1 != null){
                    for(int i = 0; i < milestones1.size();i++){
                        Milestone currentMilestone = milestones1.get(i);
                        int MilestoneIndent = smallestIndent - currentMilestone.getName().length() - currentMilestone.getId().length() + 4 - 2;
                        System.out.print(currentMilestone.getName()+"(" + currentMilestone.getId()+")");
                        printEmpty(MilestoneIndent);
                        System.out.print("|");
                        boolean print;
                        for (int j = 0; j < duration; j++){// project tasks duration
                            LocalDate day = tasksStartDate.plusDays(j);
                            print = true;
                            LocalDate milestoneDate = currentMilestone.getDate();
                            if(day.equals(milestoneDate)){
                                System.out.print(RED_BACKGROUND + "|##########|" + RESET);//12 pixels per day ?
                                print = false;

                            }
                            if(print){
                                System.out.print("            ");//12 pixels per day ?
                            }
                        }
                        System.out.println();
                        System.out.println();
                    }
                }

                for(int i= 0;i<(smallestIndent + 5);i++){//horizontal line1
                    System.out.print("_");
                }
                for (long i = 0; i < duration; i++){//horizontal line2
                    System.out.print("____________");
                }
                //Legend
                System.out.println();
                System.out.println("                 Legend:");
                System.out.println(BLUE + "                     |==========|"+ RESET + " : Planned Tasks" + RESET);
                System.out.println();
                System.out.println(RED + "                     |**********|"+ RESET + " : Actual Tasks" + RESET);

                if(milestones1 != null){
                    System.out.println();
                    System.out.println("                     " + RED_BACKGROUND +"|##########|" + RESET + " : Milestones");
                }
                //
            }else{
                System.out.println("There are no tasks in the project");
            }
        }else {
            System.out.println("There are no projects to show");
        }
        pause();
    }

//    public void monitorProgress(){
//
//        if(projects != null){
//            Project foundProject = projects.get(FIRST);
//            System.out.println();
//            double plannedSum = totalPlannedHours(foundProject);
//            double actualSum = totalActualHours(foundProject);
//
//            double plannedBudget = Math.round((plannedSum * 225.0)*100)/100.0;
//            double actualCost = Math.round((actualSum * 225.0)*100)/100.0;
//
//            LocalDate today = LocalDate.now();
//            LocalDate tasksStartDate = tasksStartAndFinishDates("start",foundProject.getTasks());
//            LocalDate tasksFinishDate = tasksStartAndFinishDates("finish",foundProject.getTasks());
//
//            //project tasks total duration
//            double projectDuration = ChronoUnit.DAYS.between(tasksStartDate, tasksFinishDate) + 1;
//            double durationTillToday = ChronoUnit.DAYS.between(tasksStartDate, today) + 1;
//            double ExecutedProgress = actualCost/plannedBudget;
//            double scheduleProgress = durationTillToday/projectDuration;
//            double earnedValue = (Math.round((plannedBudget * scheduleProgress))*100)/100.0;
//
//            System.out.println("Project budget = " + plannedBudget);
//            System.out.println("Project cost = " + actualCost);
//            System.out.println("Earned Value = " + earnedValue);
//            System.out.println("Program Executed Progress = " + Math.round(((ExecutedProgress)*100.0)*100)/100.0 +" %"); //this is only monetary wise
//            System.out.println("Program Time Based Progress = " + Math.round(((scheduleProgress)*100.0)*100)/100.0 +" %"); //this is time wise
//
//            SystemStore drake = new MiniProject.SystemStore();
//            drake.registerScheduleVariance(plannedBudget, earnedValue, plannedSum, actualSum, foundProject.getProjectID() );
//            drake.registerCostVariance(plannedBudget, earnedValue, plannedSum, actualSum, actualCost, foundProject.getProjectID());
//            System.out.println();
//            drake.printAllFinances();
//        }
//        pause();
//    }

    public void monitorCosts(){//Armin
        System.out.println("What cost is it you wish to calculate?");
        System.out.println("1. print all costs");
        System.out.println("2. Print Schedule variance");
        System.out.println("3. Print Cost Variance");
        System.out.println("4. Print Earned value");
            int option;

            final int ALL_COSTS = 1;
            final int SCHEDULE_VARIANCE = 2;
            final int COST_VARIANCE = 3;
            final int EARNED_VALUE= 4;
            final int RETURN = 5;
            do {
                System.out.print(" Type the option number: ");

                option = new KeyboardInput().Int();
                // that the user types after
                // typing the integer option.

                switch (option) {
                    case ALL_COSTS:
                        printAllCosts();
                        break;

                    case SCHEDULE_VARIANCE:
                        monitorScheduleVariance();
                        break;

                    case COST_VARIANCE:
                        monitorCostVariance();
                        break;

                    case EARNED_VALUE:
                        monitorEarnedValue();
                        break;

                    case RETURN:

                        break;

                    default:
                        System.out.println("Option " + option + " is not valid.");
                        System.out.println();
                        break;
                }
            } while (option!=RETURN);
        }


public void printAllCosts(){//Armin
    if(projects != null){
        Project foundProject = projects.get(FIRST);
        System.out.println();
        double plannedSum = totalPlannedHours(foundProject);
        double actualSum = totalActualHours(foundProject);

        double plannedBudget = Math.round((plannedSum * 225.0)*100)/100.0;
        double actualCost = Math.round((actualSum * 225.0)*100)/100.0;

        LocalDate today = LocalDate.now();
        LocalDate tasksStartDate = tasksStartAndFinishDates("start",foundProject.getTasks());
        LocalDate tasksFinishDate = tasksStartAndFinishDates("finish",foundProject.getTasks());

        //project tasks total duration
        double projectDuration = ChronoUnit.DAYS.between(tasksStartDate, tasksFinishDate) + 1;
        double durationTillToday = ChronoUnit.DAYS.between(tasksStartDate, today) + 1;
        double ExecutedProgress = actualCost/plannedBudget;
        double scheduleProgress = durationTillToday/projectDuration;
        double earnedValue = (Math.round((plannedBudget * scheduleProgress))*100)/100.0;

        System.out.println("Project budget($): " + plannedBudget);
        System.out.println("Project cost($): " + actualCost);
        System.out.println("Earned Value($): " + earnedValue);
        System.out.println("Program Executed Progress : " + Math.round(((ExecutedProgress)*100.0)*100)/100.0 +" %"); //this is only monetary wise
        System.out.println("Program Time Based Progress : " + Math.round(((scheduleProgress)*100.0)*100)/100.0 +" %"); //this is time wise
        SystemStore Costs = new MiniProject.SystemStore();
        Costs.registerCostVariance(plannedBudget, earnedValue, plannedSum, actualSum, actualCost, foundProject.getProjectID());
        Costs.registerScheduleVariance(plannedBudget, earnedValue, plannedSum, actualSum, foundProject.getProjectID() ).toString();
        //System.out.println(Costs.registerScheduleVariance(plannedBudget, earnedValue, plannedSum, actualSum, foundProject.getProjectID() ).toString());
        //System.out.println(Costs.registerCostVariance(plannedBudget, earnedValue, plannedSum, actualSum, actualCost, foundProject.getProjectID()).toString());
        Costs.printAllFinances();
    }

}

    public void monitorEarnedValue() {//Armin
        if (projects != null) {
            Project foundProject = projects.get(FIRST);
            System.out.println();
            double plannedSum = totalPlannedHours(foundProject);
            double actualSum = totalActualHours(foundProject);

            double plannedBudget = Math.round((plannedSum * 225.0) * 100) / 100.0;
            double actualCost = Math.round((actualSum * 225.0) * 100) / 100.0;

            LocalDate today = LocalDate.now();
            LocalDate tasksStartDate = tasksStartAndFinishDates("start", foundProject.getTasks());
            LocalDate tasksFinishDate = tasksStartAndFinishDates("finish", foundProject.getTasks());

            //project tasks total duration
            double projectDuration = ChronoUnit.DAYS.between(tasksStartDate, tasksFinishDate) + 1;
            double durationTillToday = ChronoUnit.DAYS.between(tasksStartDate, today) + 1;
            double ExecutedProgress = actualCost / plannedBudget;
            double scheduleProgress = durationTillToday / projectDuration;
            double earnedValue = (Math.round((plannedBudget * scheduleProgress)) * 100) / 100.0;

            System.out.println("Project budget($): " + plannedBudget);
            System.out.println("Project cost($): " + actualCost);
            System.out.println("Earned Value($): " + earnedValue);
            System.out.println("Program Executed Progress : " + Math.round(((ExecutedProgress) * 100.0) * 100) / 100.0 + " %"); //this is only monetary wise
            System.out.println("Program Time Based Progress : " + Math.round(((scheduleProgress) * 100.0) * 100) / 100.0 + " %");

            System.out.println("The Earned Value($) is ammounted to : " + earnedValue);
        }
    }


    public void monitorScheduleVariance(){//Armin
        if(projects != null){
            Project foundProject = projects.get(FIRST);
            System.out.println();
            double plannedSum = totalPlannedHours(foundProject);
            double actualSum = totalActualHours(foundProject);

            double plannedBudget = Math.round((plannedSum * 225.0)*100)/100.0;
            double actualCost = Math.round((actualSum * 225.0)*100)/100.0;

            LocalDate today = LocalDate.now();
            LocalDate tasksStartDate = tasksStartAndFinishDates("start",foundProject.getTasks());
            LocalDate tasksFinishDate = tasksStartAndFinishDates("finish",foundProject.getTasks());

             //project tasks total duration
            double projectDuration = ChronoUnit.DAYS.between(tasksStartDate, tasksFinishDate) + 1;
            double durationTillToday = ChronoUnit.DAYS.between(tasksStartDate, today) + 1;
            double ExecutedProgress = actualCost/plannedBudget;
            double scheduleProgress = durationTillToday/projectDuration;
            double earnedValue = (Math.round((plannedBudget * scheduleProgress))*100)/100.0;

            System.out.println("Project budget($): " + plannedBudget);
            System.out.println("Project cost($): " + actualCost);
            System.out.println("Earned Value($): " + earnedValue);
            System.out.println("Program Executed Progress : " + Math.round(((ExecutedProgress)*100.0)*100)/100.0 +" %"); //this is only monetary wise
            System.out.println("Program Time Based Progress : " + Math.round(((scheduleProgress)*100.0)*100)/100.0 +" %"); //this is time wise
            SystemStore Costs = new MiniProject.SystemStore();
           Costs.registerScheduleVariance(plannedBudget, earnedValue, plannedSum, actualSum, foundProject.getProjectID() ).toString();
            System.out.println(Costs.registerScheduleVariance(plannedBudget, earnedValue, plannedSum, actualSum, foundProject.getProjectID() ).toString());

        }
    }

    public void monitorCostVariance(){//Armin
        if(projects != null){
            Project foundProject = projects.get(FIRST);
            System.out.println();
            double plannedSum = totalPlannedHours(foundProject);
            double actualSum = totalActualHours(foundProject);

            double plannedBudget = Math.round((plannedSum * 225.0)*100)/100.0;
            double actualCost = Math.round((actualSum * 225.0)*100)/100.0;

            LocalDate today = LocalDate.now();
            LocalDate tasksStartDate = tasksStartAndFinishDates("start",foundProject.getTasks());
            LocalDate tasksFinishDate = tasksStartAndFinishDates("finish",foundProject.getTasks());

            //project tasks total duration
            double projectDuration = ChronoUnit.DAYS.between(tasksStartDate, tasksFinishDate) + 1;
            double durationTillToday = ChronoUnit.DAYS.between(tasksStartDate, today) + 1;
            double ExecutedProgress = actualCost/plannedBudget;
            double scheduleProgress = durationTillToday/projectDuration;
            double earnedValue = (Math.round((plannedBudget * scheduleProgress))*100)/100.0;

            System.out.println("Project budget($): " + plannedBudget);
            System.out.println("Project cost($): " + actualCost);
            System.out.println("Earned Value($): " + earnedValue);
            System.out.println("Program Executed Progress : " + Math.round(((ExecutedProgress)*100.0)*100)/100.0 +" %"); //this is only monetary wise
            System.out.println("Program Time Based Progress : " + Math.round(((scheduleProgress)*100.0)*100)/100.0 +" %"); //this is time wise
            SystemStore Costs = new MiniProject.SystemStore();
            System.out.println(Costs.registerCostVariance(plannedBudget, earnedValue, plannedSum, actualSum, actualCost, foundProject.getProjectID()).toString());
        }

    }

    public void monitorTimeSpent(){
         Project CurrentProject = projects.get(FIRST);
        if(CurrentProject != null) {
            do {
                System.out.println("Do you want to see search for a specific id or see all the members contributions? (all/specific");
                String choice = new KeyboardInput().Line();
            }while (!choice.equals("all") && !choice.equals("specific"));
            
            if(choice.equals("all")){
                ArrayList<Task> tasks = CurrentProject.getTasks();
                if(tasks != null) {
                    for (Task OneTask : tasks) {
                        System.out.println(OneTask);
                        ArrayList<TeamMemberAllocation> allocations = OneTask.getActualTeamMembers();
                        if(allocations != null) {
                            for(TeamMemberAllocation CurrentAllocation : allocations) {
                                System.out.println(CurrentAllocation.getTeamMember().getId()+ " has worked "+CurrentAllocation.getWorkHours()+" hours");
                            }
                        }
                    }
                }else {
                System.out.println("There are no tasks registered");
                }
            }else {
                System.out.println("Enter the id of team member");
                String memberId = new KeyboardInput().Line();

                while (!teamMemberExists(CurrentProject, memberId)) {
                    System.out.println("Team member does not exist or wrong id.Enter id again");
                    memberId = new KeyboardInput().Line();
                }
            double HoursOnTask = 0;
            double TotalHours = 0;
            ArrayList<Task> tasks = CurrentProject.getTasks();
            if (tasks != null) {
                for (Task OneTask : tasks) {
                    ArrayList<TeamMemberAllocation> allocations = OneTask.getActualTeamMembers();
                    if (allocations != null) {
                        for (TeamMemberAllocation CurrentAllocation : allocations) {
                            if (CurrentAllocation.getTeamMember().getId().equals(memberId)) {
                                HoursOnTask = CurrentAllocation.getWorkHours();
                                System.out.println("This member has worked " + HoursOnTask + "hours on " + OneTask);
                                TotalHours += HoursOnTask;
                            }
                        }
                    }
                }
                System.out.println("This member has worked " + TotalHours + "hours in total");
            }
        }
        pause();
    }

    public void monitorParticipation(){
        HashMap<String, Double> participation = new HashMap<>();

        Set<Entry<String, Double>> hashSet = participation.entrySet();

        if(projects != null){
            Project currentProject = projects.get(FIRST);
            System.out.println("Enter the id of a team member");
            String teamID = new KeyboardInput().Line();

            while (! teamMemberIDExists(currentProject, teamID)) {
                System.out.print("Team member does not exist or wrong ID. Enter correct ID again ");
                teamID = new KeyboardInput().Line();
            }
            String teamMemberName = "";
            double totalHours;
            ArrayList<Task> tasks = currentProject.getTasks();
            if(tasks != null){
                for(Task currentTask : tasks){
                    totalHours = 0.0;
                    ArrayList<TeamMemberAllocation> allocations = currentTask.getActualTeamMembers();
                    if(allocations != null){
                        for(TeamMemberAllocation currentAllocation : allocations){
                            if(currentAllocation.getTeamMember().getId().equals(teamID)){
                                teamMemberName = currentAllocation.getTeamMember().getName();
                                totalHours = totalHours + currentAllocation.getWorkHours();
                            }
                        }
                    }
                    if(totalHours != 0.0){
                        participation.put(currentTask.getName(), totalHours);
                    }
                }
            }
            if(! participation.isEmpty()){
                System.out.println(teamMemberName + " has participated on: ");
            }
            for(Entry entry: hashSet ) {
                System.out.println("    " + entry.getKey()+" for "+entry.getValue() + " hours");
            }
        }
        pause();
    }

    public void monitorRisk(){

        if(projects != null){ // a project is needed to talk about risk
            new RiskMatrix(projects.get(FIRST)).runRisk();
        }else{
            System.out.println("There is no registered project");
            pause();
        }
    }

      public void editInfo(){
        int option;

        final int TASKS = 1;
        final int PROJECT_DURATION = 2;
        final int TEAM_MEMBERS = 3;
        final int PROJECT = 4;
        final int RETURN = 5;


        do {
            printMenuOptions();
            System.out.print(" Type the option number: ");

            option = new KeyboardInput().Int();
            // that the user types after
            // typing the integer option.

            switch (option) {
                case TASKS:   //(DONE)
                    editTasks();
                    break;

                case PROJECT_DURATION:
                    editProjectDuration();
                    break;


                case TEAM_MEMBERS: //
                    editTeamMember();
                    break;

                case PROJECT:
                    editProject();
                    break;

                case RETURN:
                    // returnToMenu();
                    break;

                default:
                    System.out.println("Option " + option + " is not valid.");
                    System.out.println();
                    break;
            }
        } while (option!=RETURN);
    }//Armin



    public void editProject() {//HAMID
        String id ;
        do {
            System.out.println("What is the id of the project that you want to edit");
            id = new KeyboardInput().Line();
            if (!projectExists(id)) {
                System.out.println("A project with this id doesn't exists");
            }
        }while(!projectExists(id));

        System.out.println("Do you want to edit project name or id ? (name/id)");
        String option = new KeyboardInput().Line();
        Project project = retrieveProjectByID(id);
        if(option.equalsIgnoreCase("name")) {
            System.out.println("The name of this project is " +project.getName());
            System.out.println("what is the new name that you want to put  ");
            String newName = new KeyboardInput().Line();
            project.setName(newName);
            System.out.println("Project name is successfully changed to: " + newName);
        }
        if(option.equalsIgnoreCase("id")) {
            System.out.println("what is the new id that you want put ");
            String newId = new KeyboardInput().Line();
            project.setProjectID(newId);
            System.out.println("Project ID is successfully changed to: " + newID);
        }
    }//HAMID

    public void editTaskName(){
        System.out.println("Enter the id of task you wish to rename: ");
        String taskID = new KeyboardInput().Line();
        System.out.println("----------------------------------------------------------------");
        Project currentProject = projects.get(FIRST);
do
    {
    if (retrieveTask(taskID, currentProject) == null)
    {
        System.out.print("Task does not exist or wrong ID. Enter correct ID again ");
        taskID = new KeyboardInput().Line();
        System.out.println("----------------------------------------------------------------");
    }
}
while (this.retrieveTask(taskID, currentProject) == null) ;

        Task task = retrieveTask(taskID,currentProject );
        
        System.out.println(" The name of task is currently: "+task.getName());
        System.out.println("----------------------------------------------------------------");
        System.out.println("Enter new Name: ");
        
        String name = new KeyboardInput().Line();
        task.setName(name);
        System.out.println("The name is now set to: "+task.getName()+".");

    }//Armin

    public void editTeamMember(){
        System.out.println("What do you wish to edit?");
        System.out.println("1. Name "+"\n"+"2. change ID"+"\n"+"3. Remove");

        int option= new KeyboardInput().Int();
        if(option==1){
        updateTeamMemberName();
        }
        else if(option==2){
            editTeamMemberID();
        }
        else if(option==3){
            removeTeamMember();
        }
    }//Armin


    public void editTeamMemberID(){
        Project currentProject = projects.get(FIRST);
        
        System.out.println("----------------------------------------------------------------");
        System.out.println("OBS! editing ID to an existing ID will override them.");
        System.out.println("----------------------------------------------------------------");
        System.out.println("Type in id of the Team member you want to edit:");
            
        String teamMemberID = new KeyboardInput().Line();

           teamMemberIDExists(currentProject, teamMemberID);
            while(!teamMemberIDExists(currentProject, teamMemberID)) {
                System.out.print("TeamMember does not exist or wrong ID. Enter correct ID again ");
                teamMemberID = new KeyboardInput().Line();
                System.out.println("----------------------------------------------------------------");
            }

            TeamMember teamMember = retrieveTeamMember(currentProject,teamMemberID );
        System.out.println("Your team member ID is currently : "+teamMember.getId());
        System.out.println("----------------------------------------------------------------");
        System.out.println("Enter new ID: ");

        String newID = new KeyboardInput().Line();
        teamMember.setId(newID);

        System.out.println("----------------------------------------------------------------");
        System.out.println("You have now changed your ID to "+ teamMember.getId());
    }//Armin


    public void editTasks(){
        System.out.println("What do you wish to edit?");
        System.out.println("1. Name ");
        System.out.println("2. ID");
        System.out.println("3. Remove Task");
        int option= new KeyboardInput().Int();
        if(option==1){
            editTaskName();
        }
        else if(option == 2){
            editTaskID();//OSMAN
        }

        else if(option==3){
            removeTask();
        }
    }//Armin
        
    public boolean tasksIDExixsts(Project project, String ID){//OSMAN
        if(project != null){
            for(int i = 0; i < project.getTasks().size(); i++){
                if(project.getTasks().get(i).getId().equals(ID)){
                    return true;
                }
            }
        }
        return false;
    }

    public void editTaskID() {//OSMAN
        Project currentProject = projects.get(FIRST);
        System.out.println("Type in id of the task you want to edit");
        String taskID = new KeyboardInput().Line();

        tasksIDExixsts(currentProject, taskID);
        while(!tasksIDExixsts(currentProject, taskID)) {
            System.out.print("Task does not exist or wrong ID. Enter correct ID again ");
            taskID = new KeyboardInput().Line();
        }

        Task task = retrieveTask(taskID,currentProject );
        System.out.println("Enter new ID: ");

        String newID = new KeyboardInput().Line();
        task.setId(newID);
    }



public void editProjectDuration() {
    	 
     
         Project currentProject = projects.get(FIRST);
         
         LocalDate projectStartDate;
         LocalDate projectFinishDate;
         long duration;
         
             System.out.println("Enter the NEW Start date of the project: ");
             projectStartDate = new DataEvaluator().readDate();

             System.out.println("Enter the NEW Finish date of the project: ");
             projectFinishDate = new DataEvaluator().readDate();

             duration = ChronoUnit.DAYS.between(projectStartDate, projectFinishDate) + 1;
             
             currentProject.setStartDate(projectStartDate);
             currentProject.setFinishDate(projectFinishDate);
             
             currentProject.setDuration(duration);
         }





       public void removeTeamMember() {
        System.out.println("Enter the id of a team member you wish to remove");
        String memberID = new KeyboardInput().Line();
        Project currentProject = projects.get(FIRST);
        
        while (! teamMemberIDExists(currentProject, memberID))
        {
            System.out.println("Team member does not exist or wrong ID. Enter correct ID again ");
            System.out.println("----------------------------------------------------------------");
            memberID = new KeyboardInput().Line();
        }

        TeamMember member = retrieveTeamMember(currentProject, memberID);
        System.out.println("The team member you have removed is "+member.getName());
        System.out.println("----------------------------------------------------------------");

        if(memberID.equals(member.getId()))
            currentProject.getTeamMembers().remove(member);

        System.out.println("Successfully removed.");
    pause();
    }//Armin
        
        

        public void removeTask(){
        System.out.println("Enter the id of the task you wish to remove");
        String taskID = new KeyboardInput().Line();
        System.out.println("----------------------------------------------------------------");

        Project currentProject = projects.get(FIRST);
        do {
            if (retrieveTask(taskID, currentProject) == null)
            {
                System.out.print("Task does not exist or wrong ID. Enter correct ID again ");
                taskID = new KeyboardInput().Line();
            }
        }
        while (this.retrieveTask(taskID, currentProject) == null) ;

        Task task = retrieveTask(taskID,currentProject);
        System.out.println("The task you are removing is "+task.getName());
        System.out.println("----------------------------------------------------------------");

        if(taskID.equals(task.getId()))
            currentProject.getTasks().remove(task);
        System.out.println("Successfully removed.");
        
        pause();
    }//Armin

    public void updateTeamMemberName() {

            Project currentProject = projects.get(FIRST);
            System.out.println("Enter the id of a team member");
            String memberID = new KeyboardInput().Line();
            System.out.println("----------------------------------------------------------------");

            while (!teamMemberIDExists(currentProject, memberID))
            {
                System.out.print("Team member does not exist or wrong ID. Enter correct ID again ");
                memberID = new KeyboardInput().Line();
                System.out.println("----------------------------------------------------------------");
            }
            
            TeamMember member = retrieveTeamMember(currentProject, memberID);
            System.out.println("The team Member name is currently "+member.getName()+".");
            System.out.println("----------------------------------------------------------------");

            System.out.println("Enter new Name: ");
            String name = new KeyboardInput().Line();
            member.setName(name);

            System.out.println("Name changed to "+ name);
            pause();
    }//Armin

    public boolean teamMemberExists(Project project, String name){
        if(project != null){
            for(int i = 0; i < project.getTeamMembers().size(); i++){
                if(project.getTeamMembers().get(i).getName().equals(name)){
                    return true;
                }
            }
        }
        return false;
    }


    public boolean projectExists (String id){

        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).getProjectID().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean teamMemberIDExists(Project project, String ID){
        if(project != null){
            for(int i = 0; i < project.getTeamMembers().size(); i++){
                if(project.getTeamMembers().get(i).getId().equals(ID)){
                    return true;
                }
            }
        }
        return false;
    }


    public Milestone retrieveMilestone(Project project, String id) {
        if(project != null)
        {
            ArrayList<Milestone> milestones = project.getMilestones();

            if(milestones != null)
            {
                for(int i = 0; i < milestones.size(); i++)
                {
                    if(milestones.get(i).getId().equals(id))
                    {
                        return milestones.get(i);
                    }
                }
            }
            else
            {
                System.out.println("There are no registered Milestones");
            }
        }
        return null;
    }//Armin

    public TeamMember retrieveTeamMember(Project project, String id) {
        if(project != null){
            ArrayList<TeamMember> teamMembers = project.getTeamMembers();

            if(teamMembers != null){
                for(int i = 0; i < teamMembers.size(); i++){
                    if(teamMembers.get(i).getId().equals(id)){
                        return teamMembers.get(i);
                    }
                }
            } else {
                System.out.println("There are no registered team members ");
            }
        }
        return null;
    }

    public int typeOfTask(){
        int taskChoice;
        System.out.println("Choose task type:");
        System.out.println("    1. Stand Alone task independent of other tasks "); //start date & (duration or finish date)
        System.out.println("    2. Dependent task on other tasks "); //Connected to task, type of connection, duration of connection
        do {
            System.out.print("Which task type option? 1 or 2 ? ");
            taskChoice = new KeyboardInput().Int();
        } while ((taskChoice != 1 && taskChoice != 2));

        return taskChoice;
    }

    public  void updateDates(Task task, LocalDate plannedStrart, LocalDate plannedFinish, LocalDate actualStart, LocalDate actualFinish){
        task.setPlannedStart(plannedStrart);
        task.setPlannedFinish(plannedFinish);
        task.setActualStart(actualStart);
        task.setActualFinish(actualFinish);
    }

    public void checkWithProjectTimes(Project project){
        LocalDate projectStart = project.getStartDate();
        LocalDate projectFinish = project.getFinishDate();

        LocalDate projectTasksStartDate = tasksStartAndFinishDates("start", project.getTasks());
        LocalDate projectTasksFinishDate = tasksStartAndFinishDates("finish", project.getTasks());

        if(projectStart != null){
            if(projectTasksStartDate.isBefore(projectStart)){
                System.out.println();
                System.out.println("Warning !!! there exists a task that starts before the project start date. You may need to update a data!");
            }
        }

        if(projectFinish != null){
            System.out.println();
            if(projectTasksFinishDate.isAfter(projectFinish)){
                System.out.println("Warning !!! there exists a task that finishes after the project finish date. You may need to update a data!");
            }
        }

        if(projectStart == null){
            project.setStartDate(projectTasksStartDate);
        }

        if(projectFinish == null){
            project.setFinishDate(projectTasksFinishDate);
        }

        long duration = ChronoUnit.DAYS.between(project.getStartDate(), project.getFinishDate()) + 1;
        project.setDuration(duration);
    }

    public int readLengthOfTask(){
        int taskChoice;
        System.out.println("How is the length of the task defined by: ");
        System.out.println("    1. Duration ");
        System.out.println("    2. Finish date ");
        do{
            System.out.print("Which option? 1 or 2 ? ");
            taskChoice = new KeyboardInput().Int();
        }while ((taskChoice != 1 && taskChoice != 2));

        return taskChoice;
    }

    public String readConnectivityType(){
        System.out.print("Which type of connectivity does the task has? Is it SS, FS, SF, or FF ? ");
        String connectivityType = new KeyboardInput().Line();
        return new DataEvaluator().connectivityType(connectivityType);
    }

    public String readNewTaskID(Project project){
        String taskID;
        boolean repeatLoop;
        do{
            repeatLoop = false;
            System.out.print("Enter ID of the new task ");
            taskID = new KeyboardInput().Line();
            if (checkTaskExistence(taskID,project) != null){
                System.out.println();
                System.out.println("Task ID is already registered to another Task. Try again");
                repeatLoop = true;
            }
        }while(repeatLoop);

        return taskID;
    }

    public String readNewMilestoneID(Project project){
        String milestoneID;
        boolean repeatLoop;
        do{
            repeatLoop = false;
            System.out.print("Enter ID of the new Milestone ");
            milestoneID = new KeyboardInput().Line();
            if (checkMilestoneExistence(milestoneID, project) != null){
                System.out.println();
                System.out.println("Task ID is already registered to another Task. Try again");
                repeatLoop = true;
            }
        } while(repeatLoop);

        return milestoneID;
    }

    public String readExistingTaskID(Project project){
        String taskID;
        boolean repeatLoop;
        do{
            repeatLoop = false;
            System.out.print("Enter ID of existing task ");
            taskID = new KeyboardInput().Line();
            if (retrieveTask(taskID,project) == null){
                System.out.println();
                System.out.println("Task ID is not yet registered.");
                repeatLoop = true;
            }
        } while(repeatLoop);

        return taskID;
    }

    public Task retrieveTask(String taskID, Project project){
        Task foundTask = null;
        if (project.getTasks().size() != 0){
            ArrayList<Task> tasksCopy = project.getTasks();
            for (int i = 0; i < tasksCopy.size(); i++){
                if (tasksCopy.get(i).getId().equals(taskID)){
                    foundTask = tasksCopy.get(i);
                }
            }
        }else{
            System.out.println("Currently, there are no tasks registered yet");
        }
        System.out.println();
        return foundTask;
    }//NOT SURE MAYBE ARMIN

    public Task checkTaskExistence(String taskID, Project project){ //this one do not print a message if not found
        Task foundTask = null;
        if (project.getTasks().size() != 0){
            ArrayList<Task> tasksCopy = project.getTasks();
            for (int i = 0; i < tasksCopy.size(); i++){
                if (tasksCopy.get(i).getId().equals(taskID)){
                    foundTask = tasksCopy.get(i);
                }
            }
        }
        System.out.println();
        return foundTask;
    }

    public Milestone checkMilestoneExistence(String milestoneID, Project project){
        Milestone foundMilestone = null;
        ArrayList<Milestone> milestones = project.getMilestones();
        if (milestones.size() != 0){
            for (int i = 0; i < milestones.size(); i++){
                if (milestones.get(i).getId().equals(milestoneID)){
                    foundMilestone = milestones.get(i);
                }
            }
        }
        System.out.println();
        return foundMilestone;
    }

   /* public Project retrieveProject(){
        boolean continueLooping;
        Project foundProject = null;
        String projectID;
        if(projects != null){
            System.out.println();
            System.out.println("The following are the projects currently registered:");
            System.out.println("Project ID:               Project Name: " );

            for(int x = 0; x < projects.size(); x++){
                System.out.println("    " + projects.get(x).getProjectID()+ "                      " + projects.get(x).getName());
            }

            System.out.println();
            System.out.print("Enter a project ID number from above ");

            do{
                continueLooping = false;
                projectID = new KeyboardInput().Line();

                for(int i = 0; i < projects.size(); i++){
                    if (projectID.equals(projects.get(i).getProjectID())){
                        foundProject = projects.get(i);
                    }else {
                        continueLooping = true;
                    }
                }

                if(continueLooping){
                    System.out.println();
                    System.out.println("Incorrect choice, try again !");
                    System.out.print("Enter a correct project ID number");
                }
            } while (continueLooping);
        }
        return foundProject;
    }*/
     
   public Project retrieveProjectByID(String projectID) {
       for (int i = 0; i < projects.size(); i++) {
           if (projectID.equals(projects.get(i).getProjectID())) {
               Project project = projects.get(i);
               return project;
           }
       }
       return null;
   }

    public boolean checkProjectName(String name){
        if(projects != null){
            for(Project project : projects){
                if(project.getName().equals(name)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean completenessCheck(Task task){
        int outOfThree = 0;
        boolean complete = false;
        LocalDate today = LocalDate.now();

        if(task.getPlannedStart() != null){
            outOfThree++;
        }

        if(task.getPlannedDuration() != 0){
            outOfThree++;
        }

        if(task.getPlannedFinish() != null){
            outOfThree++;
        }

        if(task.getActualStart() != null){
            if(!task.getStatusOfTask()){
                task.setActualFinish(today);
            }
        }

        if(task.getActualFinish() != null){
            task.setActualDuration(ChronoUnit.DAYS.between(task.getActualStart(), task.getActualFinish()) + 1);
        }

        if(outOfThree == 2){
            if(task.getPlannedStart() != null){
                if(task.getPlannedDuration() != 0){
                    LocalDate finish = task.getPlannedStart().plusDays(task.getPlannedDuration());
                    task.setPlannedFinish(finish);
                }else{

                    long duration = ChronoUnit.DAYS.between(task.getPlannedStart(), task.getPlannedFinish()) + 1;

                    task.setPlannedDuration(duration);
                }
            }else {
                LocalDate start = task.getPlannedFinish().minusDays(task.getPlannedDuration());
                task.setPlannedStart(start);
            }
            complete = true;
        }

        if (outOfThree == 3){
            complete = true;
        }
        return complete;
    }

    public double totalPlannedHours(Project foundProject){
        double totalHours = 0.0;
        if (foundProject != null){
            if(foundProject.getTasks() != null){
                int numberOfTasks = foundProject.getTasks().size();
                for(int i = 0; i < numberOfTasks; i++){
                    Task task = foundProject.getTasks().get(i);
                    ArrayList<ManpowerAllocation> allocations = task.getPlannedManpower();
                    for(int j = 0; j < allocations.size(); j++){
                        totalHours = totalHours + allocations.get(j).getWorkHours();
                    }
                }
                //System.out.println("Total planned hours = " + totalHours);
            }else{
                System.out.println("There are no tasks in the project");
            }
        }else {
            System.out.println("There are no projects to show");
        }
        return totalHours;
    }

    public double plannedHoursTillDate(LocalDate date){
        double totalHours = 0.0;
        if (projects != null){
            Project foundProject = projects.get(FIRST);
            if(foundProject.getTasks() != null){
                int numberOfTasks = foundProject.getTasks().size();
                for(int i = 0; i < numberOfTasks; i++){
                    Task task = foundProject.getTasks().get(i);
                    ArrayList<ManpowerAllocation> allocations = task.getPlannedManpower();
                    for(int j = 0; j < allocations.size(); j++){
                        if(! allocations.get(j).getDate().isAfter(date)){
                            totalHours = totalHours + allocations.get(j).getWorkHours();
                        }
                    }
                }
                //System.out.println("Total planned hours = " + totalHours);
            }else{
                System.out.println("There are no tasks in the project");
            }
        }else {
            System.out.println("There are no projects to show");
        }
        return totalHours;
    }

    public double totalActualHours(Project foundProject){
        double totalHours = 0.0;
        if (foundProject != null){
            if(foundProject.getTasks() != null){
                int numberOfTasks = foundProject.getTasks().size();
                for(int i = 0; i < numberOfTasks; i++){
                    Task task = foundProject.getTasks().get(i);
                    ArrayList<TeamMemberAllocation> teamMembers = task.getActualTeamMembers();
                    for(int j = 0; j < teamMembers.size(); j++){
                        totalHours = totalHours + teamMembers.get(j).getWorkHours();
                    }
                }
                System.out.println("Total Actual hours = " + totalHours);
            }else{
                System.out.println("There are no tasks in the project");
            }
        }else {
            System.out.println("There are no projects to show");
        }
        return totalHours;
    }

    public double actualHoursTillDate(LocalDate date){
        double totalHours = 0.0;
        if (projects != null){
            Project foundProject = projects.get(FIRST);
            if(foundProject.getTasks() != null){
                int numberOfTasks = foundProject.getTasks().size();
                for(int i = 0; i < numberOfTasks; i++){
                    Task task = foundProject.getTasks().get(i);
                    ArrayList<TeamMemberAllocation> teamMembers = task.getActualTeamMembers();
                    for(int j = 0; j < teamMembers.size(); j++){
                        if(! teamMembers.get(j).getDate().isAfter(date)){
                            totalHours = totalHours + teamMembers.get(j).getWorkHours();
                        }
                    }
                }
                //System.out.println("Total Actual hours = " + totalHours);
            }else{
                System.out.println("There are no tasks in the project");
            }
        }else {
            System.out.println("There are no projects to show");
        }
        return totalHours;
    }

   /* public double earnedValue(LocalDate date){

        /*Earned Value (EV)
          Also known as Budgeted Cost of Work Performed (BCWP),
          Earned Value is the amount of the task that is actually completed.
          It is also calculated from the project budget.
          EV = Percent Complete (actual) x Project Budget */

        /*System.out.print("Enter progress on date " + date + " : (0.0 - 100.0) ");
        double percent = new KeyboardInput().positiveDouble();
        while (percent > 100.0){
            System.out.println("Enter value between 0.0 and 100.0. try again ");
            percent = new KeyboardInput().positiveDouble();
        }

        percent = percent/100.0;

        return  Math.round((projects.get(FIRST).getBudget() * percent) * 100.0)/100.0;
    }*/

    public double actualCost(LocalDate date){

        /*Actual Cost (AC)
        Also known as Actual Cost of Work Performed (ACWP),
        Actual Cost is the actual to-date cost of the task.
        AC = Actual Cost of the Task*/

        double actualSum = actualHoursTillDate(date);

        return  Math.round(actualSum * 189.0 * 100.0)/100.0;
    }

   /* public double scheduleVariance (LocalDate date){
        /*Schedule Variance (SV)
        In this, the first output calculated in the earned value analysis,
        SV tells the amount that the project is ahead or behind schedule.
        SV = EV – PV
        but PV is Planned Value (PV)
        Also known as Budgeted Cost of Work Scheduled (BCWS),
        Planned Value is the amount of the task that is supposed to have been completed, in terms of the project budget.
        It is calculated from the project budget.
        PV = Percent Complete (planned) x project Budget*/ // PV is schedule days wise

        //first we calculate PV
       /* double plannedTotalSum = totalPlannedHours(projects.get(FIRST));
        double actualTillDateSum = actualHoursTillDate(date);
        double plannedPercentComplete = actualTillDateSum/plannedTotalSum;

        double plannedValueTillDate = Math.round((plannedPercentComplete * projects.get(FIRST).getBudget()) * 100.0)/100.0;

        return earnedValue(date) - plannedValueTillDate;
    }*/

    /*public double costVariance(LocalDate date){
        /*Cost Variance (CV)
        Similar to the schedule variance, the Cost Variance tells how far
        the project is over or under budget.
        CV = EV – AC*/

       /* return (earnedValue(date) - actualCost(date));
    }*/

    public LocalDate choiceOfDate(){

        ArrayList<Task> tasks = projects.get(FIRST).getTasks();

        LocalDate today = LocalDate.now();
        LocalDate tasksStartDate = tasksStartAndFinishDates("start", tasks);
        LocalDate tasksFinishDate = tasksStartAndFinishDates("finish", tasks);

        System.out.println();
        System.out.print("Enter the date of interest ");
        LocalDate dateOfInterest = new DataEvaluator().readDate();

        while(! dateOfInterest.isAfter(tasksStartDate) && ! dateOfInterest.isAfter(today) && ! dateOfInterest.isBefore(tasksFinishDate)){
            System.out.println("Choice of date should be between " + tasksStartDate + " and " + tasksFinishDate);
            System.out.print("Enter the info again ");
            dateOfInterest = new DataEvaluator().readDate();
        }
        return dateOfInterest;
    }

    public void printEmpty(int space){
        for(int i = 0; i < space; i++){
            System.out.print(" ");
        }
    }

    public void pause (){
        System.out.println();
        System.out.println("Enter to continue... ");
        new KeyboardInput().enter();
    }

    public String readQualification(){
        int choice;
        String result = "";
        do {
            System.out.println("List of qualifications:");
            System.out.println("    1. Software Developer");
            System.out.println("    2. Video Game Developer");
            System.out.println("    3. Software Engineer");
            System.out.println("    4. Requirements Engineer");
            System.out.println("    5. Test Engineer");
            System.out.println("    6. Network Administrator");
            System.out.println();
            System.out.print("Enter qualification number ");
            choice = new KeyboardInput().positiveInt();
        }while(choice > 6);

        switch (choice){
            case 1:
                result = "Software Developer";
                break;
            case 2:
                result = "Video Game Developer";
                break;
            case 3:
                result = "Software Engineer";
                break;
            case 4:
                result = "Requirements Engineer";
                break;
            case 5:
                result = "Test Engineer";
                break;
            case 6:
                result = "Network Administrator";
                break;
            default:
                System.out.println("You may want to choose the correct number !");
                break;
        }
        return result;
    }

    public LocalDate tasksStartAndFinishDates (String startOrFinish, ArrayList<Task> tasks){
        int numberOfTasks = tasks.size();
        ArrayList<LocalDate> starts = new ArrayList<>();
        ArrayList<LocalDate> finishes = new ArrayList<>();
        LocalDate plannedStartDate, actualStartDate;
        LocalDate plannedFinishDate, actualFinishDate;
        LocalDate result = null;

        for (int i = 0; i < numberOfTasks; i++){
            Task tas = tasks.get(i);

            plannedStartDate = tas.getPlannedStart();
            plannedFinishDate = tas.getPlannedFinish();

            if(tas.getActualStart() != null){
                actualStartDate = tas.getActualStart();
                starts.add(actualStartDate);
            }

            if(tas.getActualFinish() != null){
                actualFinishDate = tas.getActualFinish();
                finishes.add(actualFinishDate);
            }

            starts.add(plannedStartDate);
            finishes.add(plannedFinishDate);
        }

        if(startOrFinish.equals("start") && starts.size() > 0){
            Collections.sort(starts);
            result = starts.get(0);
        }else if(startOrFinish.equals("finish") && finishes.size() > 0){
            int numberOfFinishes = finishes.size();
            Collections.sort(finishes);
            result = finishes.get(numberOfFinishes - 1);
        }
        return result;
    }

    public boolean readActualTaskStatus(){
        boolean repeat;
        int option;
        System.out.println("What is the status of the task ?");
        do{
            repeat = false;
            System.out.println("    1. Completed ");
            System.out.println("    2. Active ");
            System.out.println("Enter status option");
            option = new KeyboardInput().positiveInt();
            if(option > 2){
                repeat = true;
            }
        } while (repeat);
        if (option == 1){
            return true;
        } else{
            return false;
        }
    }

    public void projectCompletenessCheck(){
        if(projects != null){
            for(int m = 0; m < projects.size(); m++){
                for(int i = 0; i < projects.get(m).getTasks().size(); i++){
                    completenessCheck(projects.get(m).getTasks().get(i));
                }

                Project currentProject = projects.get(m);

                if(currentProject.getFinishDate() == null){

                    LocalDate finish = tasksStartAndFinishDates("finish",currentProject.getTasks());
                    currentProject.setFinishDate(finish);

                    long duration = ChronoUnit.DAYS.between(currentProject.getStartDate(), finish) + 1;
                    currentProject.setDuration(duration);
                }
            }
        }
    }

    public void writeProjectToJsonFile(){

        try {
            FileWriter file = new FileWriter("MiniProject/MiniProject.json");

            Gson gsonStructured = new GsonBuilder().setPrettyPrinting().create();
            file.write(gsonStructured.toJson(projects));
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFromJsonFile()throws Exception {
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader("MiniProject/MiniProject.json"));
        projects = gson.fromJson(br, new TypeToken<ArrayList<Project>>(){}.getType());
    }

    public void readFromSystemClass(){
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.parse("2018-11-15");
        Project mgmtTool = new Project("Project Management Tool Development","1", startDate);
        mgmtTool.setBudget(401175.0); //SEK budget
        projects.add(mgmtTool);


        Task general = new Task("General Tasks","1",1);
        Task cost = new Task("Cost Related","2",1);
        Task risk = new Task("Risk Matrix","3",1);
        Task part = new Task("Participation on tasks","4",1);
        Task spent = new Task("Time spent on project","5",1);
        Task schedule = new Task("Project schedule","6",1);

        Milestone milestone1 = new Milestone("Executing on Console","1",LocalDate.parse("2018-12-10"));
        Milestone milestone2 = new Milestone("Executing using json","2",LocalDate.parse("2018-12-21"));
        Milestone milestone3 = new Milestone("Executing graphically","3",LocalDate.parse("2019-01-07"));

        mgmtTool.getMilestones().add(milestone1);
        mgmtTool.getMilestones().add(milestone2);
        mgmtTool.getMilestones().add(milestone3);

        mgmtTool.getTasks().add(general);
        mgmtTool.getTasks().add(cost);
        mgmtTool.getTasks().add(risk);
        mgmtTool.getTasks().add(part);
        mgmtTool.getTasks().add(spent);
        mgmtTool.getTasks().add(schedule);

        general.setPlannedStart(LocalDate.parse("2018-11-16"));
        general.setPlannedFinish(LocalDate.parse("2019-01-20"));

        cost.setPlannedStart(LocalDate.parse("2018-11-26"));
        cost.setPlannedFinish(LocalDate.parse("2019-01-11"));

        risk.setPlannedStart(LocalDate.parse("2018-11-26"));
        risk.setPlannedFinish(LocalDate.parse("2019-01-11"));

        part.setPlannedStart(LocalDate.parse("2018-11-26"));
        part.setPlannedFinish(LocalDate.parse("2019-01-11"));

        spent.setPlannedStart(LocalDate.parse("2018-11-26"));
        spent.setPlannedFinish(LocalDate.parse("2019-01-11"));

        schedule.setPlannedStart(LocalDate.parse("2018-11-26"));
        schedule.setPlannedFinish(LocalDate.parse("2019-01-11"));

        //
        general.setActualStart(LocalDate.parse("2018-11-16"));
        general.setActualFinish(today);

        cost.setActualStart(LocalDate.parse("2018-11-26"));
        cost.setActualFinish(today);

        risk.setActualStart(LocalDate.parse("2018-11-26"));
        risk.setActualFinish(today);

        part.setActualStart(LocalDate.parse("2018-11-26"));
        part.setActualFinish(today);

        spent.setActualStart(LocalDate.parse("2018-11-26"));
        spent.setActualFinish(today);

        schedule.setActualStart(LocalDate.parse("2018-11-26"));
        schedule.setActualFinish(today);

        completenessCheck(general);
        completenessCheck(cost);
        completenessCheck(risk);
        completenessCheck(part);
        completenessCheck(spent);
        completenessCheck(schedule);

        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-16")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-17")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-18")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-19")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-20")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-21")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-22")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-23")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-24")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-25")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-26")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-27")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-28")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-29")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-11-30")));

        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-01")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-02")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-03")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-04")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-05")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-06")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-07")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-08")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-09")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-10")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-11")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-12")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-13")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-14")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-15")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-16")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-17")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-18")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-19")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-20")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-21")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-22")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-23")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-24")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-25")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-26")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-27")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-28")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-29")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-30")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-12-31")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2018-01-01")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-02")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-03")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-04")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-05")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-06")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-07")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-08")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-09")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-10")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-11")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-12")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-13")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-14")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-15")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-16")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-17")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),20,LocalDate.parse("2019-01-18")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2019-01-19")));
        general.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),10,LocalDate.parse("2019-01-20")));


        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-26")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-27")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-28")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-29")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-30")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-01")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-02")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-03")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-04")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-05")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-06")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-07")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-08")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-09")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-10")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-11")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-12")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-13")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-14")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-15")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-16")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-17")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-18")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-19")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-20")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-21")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-22")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-23")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-24")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-25")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-26")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-27")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-28")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-29")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-30")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-31")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-01-01")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-02")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-03")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-04")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-05")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-06")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-07")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-08")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-09")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-10")));
        cost.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-11")));

        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-26")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-27")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-28")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-29")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-30")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-01")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-02")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-03")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-04")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-05")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-06")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-07")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-08")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-09")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-10")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-11")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-12")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-13")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-14")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-15")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-16")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-17")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-18")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-19")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-20")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-21")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-22")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-23")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-24")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-25")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-26")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-27")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-28")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-29")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-30")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-31")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-01-01")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-02")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-03")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-04")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-05")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-06")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-07")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-08")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-09")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-10")));
        risk.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-11")));

        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-26")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-27")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-28")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-29")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-30")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-01")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-02")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-03")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-04")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-05")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-06")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-07")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-08")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-09")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-10")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-11")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-12")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-13")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-14")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-15")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-16")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-17")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-18")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-19")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-20")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-21")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-22")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-23")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-24")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-25")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-26")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-27")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-28")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-29")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-30")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-31")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-01-01")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-02")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-03")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-04")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-05")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-06")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-07")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-08")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-09")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-10")));
        part.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-11")));


        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-26")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-27")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-28")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-29")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-30")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-01")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-02")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-03")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-04")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-05")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-06")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-07")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-08")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-09")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-10")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-11")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-12")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-13")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-14")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-15")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-16")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-17")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-18")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-19")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-20")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-21")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-22")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-23")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-24")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-25")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-26")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-27")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-28")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-29")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-30")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-31")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-01-01")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-02")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-03")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-04")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-05")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-06")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-07")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-08")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-09")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-10")));
        spent.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-11")));

        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-26")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-27")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-28")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-29")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-11-30")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-01")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-02")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-03")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-04")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-05")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-06")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-07")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-08")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-09")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-10")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-11")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-12")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-13")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-14")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-15")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-16")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-17")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-18")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-19")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-20")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-21")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-22")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-23")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-24")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-25")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-26")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-27")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-28")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-29")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-30")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-12-31")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),3,LocalDate.parse("2018-01-01")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-02")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-03")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-04")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-05")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-06")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-07")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-08")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-09")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-10")));
        schedule.getPlannedManpower().add(new ManpowerAllocation(new Manpower("TBA"),4,LocalDate.parse("2019-01-11")));

        TeamMember armin = new TeamMember("Armin Ghoroghi","1","Software Developer");
        TeamMember james = new TeamMember("James Wagabaza","2","Software Developer");
        TeamMember osman = new TeamMember("Osman Osman","3","Software Developer");
        TeamMember hamid = new TeamMember("Hamidreza Yaghoobzadeh","4","Software Developer");
        TeamMember eyuell = new TeamMember("Eyuell Hailemichael","5","Software Developer");

        mgmtTool.getTeamMembers().add(armin);
        mgmtTool.getTeamMembers().add(james);
        mgmtTool.getTeamMembers().add(osman);
        mgmtTool.getTeamMembers().add(hamid);
        mgmtTool.getTeamMembers().add(eyuell);

        long numberOfDays = ChronoUnit.DAYS.between(LocalDate.parse("2018-11-26"), today) + 1;

        //the hours here are just to make different times for each team member
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-11-26")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-11-27")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-11-28")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-11-29")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-11-30")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-12-01")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-12-02")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-12-03")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-12-04")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-12-05")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-12-06")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-12-07")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-12-08")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-12-09")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(armin, 1, LocalDate.parse("2018-12-10")));

        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-11-26")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-11-27")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-11-28")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-11-29")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-11-30")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-12-01")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-12-02")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-12-03")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-12-04")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-12-05")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-12-06")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-12-07")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-12-08")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-12-09")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(james, 1, LocalDate.parse("2018-12-10")));

        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-11-26")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-11-27")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-11-28")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-11-29")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-11-30")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-12-01")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-12-02")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-12-03")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-12-04")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-12-05")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-12-06")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-12-07")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-12-08")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-12-09")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(osman, 1, LocalDate.parse("2018-12-10")));

        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-11-26")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-11-27")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-11-28")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-11-29")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-11-30")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-12-01")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-12-02")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-12-03")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-12-04")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-12-05")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-12-06")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-12-07")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-12-08")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-12-09")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 1, LocalDate.parse("2018-12-10")));

        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-16")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-17")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-18")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-19")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-20")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-21")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-22")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-23")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-24")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-25")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-26")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-27")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-28")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-29")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-11-30")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-12-01")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-12-02")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-12-03")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-12-04")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-12-05")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-12-06")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-12-07")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-12-08")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-12-09")));
        general.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 1, LocalDate.parse("2018-12-10")));

        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-11-26")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-11-27")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-11-28")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-11-29")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-11-30")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-12-01")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-12-02")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-12-03")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-12-04")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-12-05")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-12-06")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-12-07")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-12-08")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-12-09")));
        cost.getActualTeamMembers().add(new TeamMemberAllocation(armin, 3.1, LocalDate.parse("2018-12-10")));

        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-11-26")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-11-27")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-11-28")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-11-29")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-11-30")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-12-01")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-12-02")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-12-03")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-12-04")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-12-05")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-12-06")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-12-07")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-12-08")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-12-09")));
        risk.getActualTeamMembers().add(new TeamMemberAllocation(james, 3.2, LocalDate.parse("2018-12-10")));

        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-11-26")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-11-27")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-11-28")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-11-29")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-11-30")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-12-01")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-12-02")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-12-03")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-12-04")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-12-05")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-12-06")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-12-07")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-12-08")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-12-09")));
        part.getActualTeamMembers().add(new TeamMemberAllocation(osman, 3.3, LocalDate.parse("2018-12-10")));

        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-11-26")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-11-27")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-11-28")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-11-29")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-11-30")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-12-01")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-12-02")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-12-03")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-12-04")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-12-05")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-12-06")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-12-07")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-12-08")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-12-09")));
        spent.getActualTeamMembers().add(new TeamMemberAllocation(hamid, 3.4, LocalDate.parse("2018-12-10")));

        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-11-26")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-11-27")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-11-28")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-11-29")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-11-30")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-12-01")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-12-02")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-12-03")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-12-04")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-12-05")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-12-06")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-12-07")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-12-08")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-12-09")));
        schedule.getActualTeamMembers().add(new TeamMemberAllocation(eyuell, 3.5, LocalDate.parse("2018-12-10")));

        mgmtTool.getRisks().add(new Risk("1","Lack of Trust",0.7,7));
        mgmtTool.getRisks().add(new Risk("2","Conflict and tension",0.8,9));
        mgmtTool.getRisks().add(new Risk("3","Lack of Commitment",0.9,9));
        mgmtTool.getRisks().add(new Risk("4","Weak Information sharing",0.6,7));
        mgmtTool.getRisks().add(new Risk("5","Misalignment with team-Goal",0.7,7));
        mgmtTool.getRisks().add(new Risk("6","Lack of Team spirit",0.6,8));
        mgmtTool.getRisks().add(new Risk("7","Lack of Organisation",0.6,7));
        mgmtTool.getRisks().add(new Risk("8","Being out of schedule",0.7,8));
        mgmtTool.getRisks().add(new Risk("9","Lack of Knowledge",0.7,7));
    }

}
