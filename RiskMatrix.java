package MiniProject;

import java.util.ArrayList;

public class RiskMatrix {
    // Background
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m"; //"\033[42m" Green
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m"; // Yellow
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m"; // Red
    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN

    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

    // Bold
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK

    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    public static final int REGISTER_RISK = 1;
    public static final int PRINT_RISKS = 2;
    public static final int EDIT_RISKS = 3;
    public static final int CONVERT_PROBABILITY_AND_IMPACT_TO_PERCENTAGE = 4;
    public static final int QUIT = 5;
    public static final int INITIAL = 0;
    public static final int FIRST = 1;
    public static final int IMPACT_SCALE = 25;
    public static final int PROBABILITY_SCALE = 120;
    public static final double IMPACT_LIMIT = 10.0;

    private Project currentProject;

    public RiskMatrix(Project currentProject){
        this.currentProject = currentProject;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void printMenu (){
        System.out.println("SELECT FROM THE FOLLOWING OPTIONS");
        System.out.println();
        System.out.println("    1. REGISTER RISK.");
        System.out.println("    2. PRINT RISK.");
        System.out.println("    3. EDIT RISK.");
        System.out.println("    4. CONVERT PROBABILITY AND IMPACT TO PERCENTAGE VALUES.");
        System.out.println("    5. QUIT.");
        System.out.println();
    }

    public void runRisk(){ // this methods handles all the risk matrix functions.

        int option;
        int loopCounter = 0;

        do {
            loopCounter++;
            if (loopCounter == FIRST){
                System.out.println();
                System.out.println("========= RISK MATRIX =========");
                System.out.println();
            }

            printMenu();
            System.out.print("Enter an option from the above: ");
            option = new KeyboardInput().positiveInt();

            switch (option){
                case REGISTER_RISK:
                    registerRisk();
                    System.out.println("**************************************");
                    break;

                case PRINT_RISKS:
                    printFunction();
                    System.out.println("**************************************");
                    break;

                case EDIT_RISKS:
                    editRisk();
                    System.out.println("**************************************");

                case CONVERT_PROBABILITY_AND_IMPACT_TO_PERCENTAGE:
                    percentageConverter();
                    System.out.println("**************************************");
                    break;

                case QUIT:
                    System.out.println("**************************************");
                    break;

                default:
                    System.out.println("Enter the right value");
                    break;
            }
        } while (option != QUIT);
    }

    public void registerRisk ( ){ //  this method handles risk registration

        if(currentProject != null){
            String riskID = readRiskID();

            String riskName = readRiskName();

            String riskType = readRiskType();

            System.out.print("Risk Probability (b/n 0.0 and 1.0) : ");
            double probability = new KeyboardInput().positiveDouble();
            probability = new RiskEvaluator().probability(probability);

            System.out.print("Risk Impact (b/n 0.0 and 10.0):");
            double impact = new KeyboardInput().positiveDouble();
            impact = new RiskEvaluator().impact(impact);

            currentProject.getRisks().add(new Risk(riskID, riskName, riskType, probability, impact));
        }
    }

    public void printFunction (){
        int option;
        final int PRINT_RISK_MATRIX = 1;
        final int PRINT_A_SPECIFIC_RISK = 2;
        final int PRINT_ALL_REGISTERED_RISKS = 3;
        final int PRINT_NUMBER_OF_REGISTERED_RISKS = 4;
        final int QUIT_PRINT_FUNCTION = 5;
        if(currentProject != null){
            ArrayList<Risk> risks = currentProject.getRisks();
            if(risks != null){
                do {
                    System.out.println("SELECT FROM THE FOLLOWING OPTIONS.");
                    System.out.println();
                    System.out.println("    1. PRINT GRAPHICAL RISK MATRIX ");
                    System.out.println("    2. PRINT A SPECIFIC RISK ");
                    System.out.println("    3. PRINT ALL REGISTERED RISKS ");
                    System.out.println("    4. DISPLAY NUMBER OF REGISTERED RISKS ");
                    System.out.println("    5. QUIT PRINT FUNCTION ");
                    System.out.println();
                    option = new KeyboardInput().positiveInt();

                    switch (option){

                        case PRINT_RISK_MATRIX:
                            printRiskMatrix();
                            System.out.println("*************************************************************************");
                            break;

                        case PRINT_A_SPECIFIC_RISK:
                            printSpecificRisk();
                            System.out.println("**************************************************************************************************");
                            break;

                        case PRINT_ALL_REGISTERED_RISKS:
                            printAllRisks();
                            System.out.println("**************************************************************************************************");
                            break;

                        case PRINT_NUMBER_OF_REGISTERED_RISKS:
                            printNumberOfRisk();
                            System.out.println("*******************************");
                            break;

                        case QUIT_PRINT_FUNCTION:
                            System.out.println("Exiting.....");
                            break;

                        default:
                            System.out.println("Enter a correct option.");
                            break;
                    }
                    new ProjectManagementTool().pause();
                } while (option!=QUIT_PRINT_FUNCTION);
            } else {
                System.out.println("There is no registered risk.");
            }
        } else {
            System.out.println("There is no registered project.");
        }
    }

    public void editRisk (){
        final int NAME = 1;
        final int PROBABILITY = 2;
        final int IMPACT = 3;
        final int TYPE = 4;
        final int STATUS = 5;
        final int REMOVE = 6;
        final int QUIT_EDIT = 7;
        int option;

        if(currentProject != null){
            ArrayList<Risk> risks = currentProject.getRisks();
            if(risks != null){
                do {
                    System.out.println("What would you like to edit?");
                    System.out.println();
                    System.out.println("    1. Edit Risk Name.");
                    System.out.println("    2. Edit Risk Probability.");
                    System.out.println("    3. Edit Risk Impact.");
                    System.out.println("    4. Edit Risk Type.");
                    System.out.println("    5. Update Risk Status.");
                    System.out.println("    6. Remove Risk.");
                    System.out.println("    7. Quit Editing");
                    option = new KeyboardInput().positiveInt();

                    switch (option){
                        case NAME:
                            boolean repeat;
                            do{
                                repeat = false;
                                String riskID = checkRiskID();
                                Risk foundRisk = retrieveRiskByID(riskID, currentProject.getRisks());
                                if (foundRisk != null) {
                                    System.out.print("Enter new name: ");
                                    String newName = new KeyboardInput().Line();
                                    newName = new RiskEvaluator().name(newName);
                                    foundRisk.setRiskName(newName);
                                    System.out.println("You have successfully updated the risk's name to " + foundRisk.getRiskName());
                                } else {
                                    System.out.println("The risk name your are trying to access is not registered");
                                    repeat = true;
                                }
                            } while (repeat);
                            break;

                        case PROBABILITY://
                            do{
                                repeat = false;
                                String riskID = checkRiskID();
                                Risk foundRisk = retrieveRiskByID(riskID, currentProject.getRisks());
                                if (foundRisk != null ){
                                    System.out.print("Enter the new probability value: ");
                                    double newProbability = new KeyboardInput().positiveDouble();
                                    newProbability = new RiskEvaluator().probability(newProbability);
                                    foundRisk.setProbability(newProbability);
                                    System.out.println("You have successfully updated the risk's probability to " + foundRisk.getProbability());
                                } else {
                                    System.out.println("The risk name you are trying to access is not registered.");
                                    repeat = true;
                                }
                            } while (repeat);
                            break;

                        case IMPACT:
                            do{
                                repeat = false;
                                String riskID = checkRiskID();
                                Risk foundRisk = retrieveRiskByID(riskID, currentProject.getRisks());
                                if(foundRisk != null){
                                    System.out.print("Enter the new impact value : ");
                                    double newImpact = new KeyboardInput().positiveDouble();
                                    newImpact = new RiskEvaluator().impact(newImpact);
                                    foundRisk.setImpact(newImpact);
                                    System.out.println("You have successfully updated the risk's impact to " + foundRisk.getImpact());
                                } else {
                                    System.out.println("The risk name you are trying to access is not registered.");
                                    repeat = true;
                                }
                            } while (repeat);
                            break;
                        case TYPE:
                        {
                            String riskID = checkRiskID();
                            Risk foundRisk = retrieveRiskByID(riskID, currentProject.getRisks());
                            if(foundRisk != null){
                                System.out.println("Do you want to change the current type of risk : (" + foundRisk.getRiskType() + ") ?");
                                System.out.print("Enter Y or N ");
                                String choice = new KeyboardInput().Line();
                                choice = new DataEvaluator().yesOrNo(choice);
                                if(choice.equals("Y")){
                                    if(foundRisk.getRiskType().equals("Predicted")){
                                        foundRisk.setRiskType("   New   "); //intentional spaces
                                    } else {
                                        foundRisk.setRiskType("Predicted");
                                    }
                                    System.out.println("Risk type successfully changed to: " + foundRisk.getRiskType());
                                } else {
                                    System.out.println("Editing the type has been left unchanged");
                                }
                            }
                        }
                        break;
                        case STATUS:
                        {
                            final int TBM = 1;
                            final int MANAGED = 2;
                            final int COMMUNICATE = 3;
                            final int EFFORT = 4;
                            final int GONE = 5;
                            final int QUIT = 6;

                            String riskID = checkRiskID();
                            Risk foundRisk = retrieveRiskByID(riskID, currentProject.getRisks());
                            if (foundRisk != null){
                                int choice;
                                do{
                                    System.out.println("  Options of Risk Status update: ");
                                    System.out.println("    1. To be Managed,");
                                    System.out.println("    2. Managed,");
                                    System.out.println("    3. Communicated,");
                                    System.out.println("    4. Effort Made,");
                                    System.out.println("    5. Disappeared,");
                                    System.out.println("    6. Quit Updating.");
                                    System.out.println("Choose option of Status ");
                                    choice = new KeyboardInput().positiveInt();
                                } while (choice > QUIT);

                                switch (choice){
                                    case TBM:
                                        foundRisk.setRiskStatus("To Be Managed");
                                        break;

                                    case MANAGED:
                                        foundRisk.setRiskStatus("Managed");
                                        break;

                                    case COMMUNICATE:
                                        foundRisk.setRiskStatus("Communicated");
                                        break;

                                    case EFFORT:
                                        foundRisk.setRiskStatus("Effort Made");
                                        break;
                                    case GONE:
                                        foundRisk.setRiskStatus("Disappeared");
                                        break;

                                    default: //if not all the above
                                        System.out.println("Exiting....");
                                }

                                if(choice < QUIT){
                                    System.out.println("Status successfully updated to : " + foundRisk.getRiskStatus());
                                }
                            }
                        }
                        break;

                        case REMOVE:
                            do{
                                repeat = false;
                                String riskID = checkRiskID();
                                Risk foundRisk = retrieveRiskByID(riskID, currentProject.getRisks());
                                if ( foundRisk != null){
                                    System.out.println("Are you sure you want to delete " + foundRisk.getRiskName() + "?" );
                                    deleteRisk(foundRisk, currentProject);
                                } else {
                                    System.out.println("The risk name you trying to access is not registered.");
                                    repeat = true;
                                }
                            }  while (repeat);
                            break;

                        case QUIT_EDIT:
                            System.out.println("*********************************************************");
                            break;

                        default:
                            System.out.println("Enter the right option");
                            break;
                    }
                    new ProjectManagementTool().pause();
                } while (option != QUIT_EDIT);
            } else {
                System.out.println("There is no registered risk.");
            }
        } else {
            System.out.println("There is no registered project.");
        }
    }

    public void deleteRisk(Risk foundRisk, Project project){ // this methods is handles the risk deletion functions
        if(project != null){
            final int YES = 1;
            int option;
            System.out.println("Enter" + " 1 " + "to delete risk,");
            System.out.print("Enter any other number to abort. ");
            option = new KeyboardInput().Int();
            if (option == YES){
                project.getRisks().remove(foundRisk);
                System.out.println("Risk deleted!.");

            } else {
                System.out.println("Process aborted.");
            }
        }
    }

    public Risk retrieveRegisteredRisk(String riskName, Project project){
        if(project != null){
            ArrayList<Risk> risks = project.getRisks();
            if ( risks != null){
                for(Risk risk : risks){
                    if (risk.getRiskName().equals(riskName)){
                        return risk;
                    }
                }
            }
        }
        return null;
    }

    public String readRiskID (){// for new risk ID
        String riskID = "";
        if(currentProject != null){
            System.out.print("Enter new risk ID ");
            riskID = new KeyboardInput().Line();
            while (retrieveRiskByID(riskID, currentProject.getRisks()) != null){
                System.out.println("ID is already registered by other risk.");
                System.out.print("Enter another risk ID ");
                riskID = new KeyboardInput().Line();
            }
        }
        return riskID;
    }

    public String checkRiskID (){ // for existing risk ID
        String riskID = "";
        if(currentProject != null){
            ArrayList<Risk> risks = currentProject.getRisks();
            if(risks != null){
                System.out.println("=================================");
                System.out.println(WHITE_UNDERLINED + "    List of Risks (ID and Name)  " + CYAN_BRIGHT);
                for (Risk risk: risks) {
                    System.out.println(risk.getRiskID() + " :- " + risk.getRiskName());
                }
                System.out.println();
                System.out.print("Form the above list of Risks, enter risk ID ");
                riskID = new KeyboardInput().Line();
                while (retrieveRiskByID(riskID, risks) == null){
                    System.out.println("The provided ID does not exist.");
                    System.out.print("Enter risk ID correctly ");
                    riskID = new KeyboardInput().Line();
                }
            } else {
                System.out.println("There are no registered risks ");
            }
        }
        return riskID;
    }

    public  String  readRiskName (){ // this methods handles all name input requests.
        System.out.print("Enter Risk Name: ");
        String name = new KeyboardInput().Line();
        name = new RiskEvaluator().name(name);

        while (retrieveRegisteredRisk(name, currentProject) != null) {
            System.out.println();
            System.out.println("There exists a risk with the same name use, another name. ");
            System.out.println();
            System.out.print("Enter new Risk Name.");
            name = new KeyboardInput().Line();
            name = new RiskEvaluator().name(name);
        }
        return name;
    }

    public  String  readRiskType (){ // read new risk type
        String type;
        do{
            System.out.println("  List of Risk Types");
            System.out.println("    1. PREDICTED ");
            System.out.println("    2. NEW ");
            System.out.print("Enter the type of risk (1 or 2 ?) ");
            type = new KeyboardInput().Line();
        } while (! type.equals("1") && ! type.equals("2") );

        if(type.equals("1")){
            return "Predicted";
        } else {
            return "   New   "; //intentional spaces
        }
    }

    public String readRisk (){ // for Existing risk names
        System.out.print("Enter Risk Name :");
        String name = new KeyboardInput().Line();
        name = new RiskEvaluator().name(name);
        return name ;
    }

    public Risk retrieveRiskByID(String riskID, ArrayList<Risk> risks){
        for(int i = 0; i < risks.size(); i++){
            Risk currentRisk = risks.get(i);
            if(currentRisk.getRiskID().equals(riskID)){
                return currentRisk;
            }
        }
        return null;
    }

    public void printRiskMatrix(){
        ArrayList<Risk> risks = currentProject.getRisks();
        if(risks != null){
            System.out.println("        IMPACT");
            System.out.println("          ʌ");
            System.out.println("          │");
            System.out.print("          │");
            for(int i = IMPACT_SCALE; i >= INITIAL ;i--){
                System.out.println();
                if(i == 23){
                    System.out.print("Extensive │");
                } else if (i == 18){
                    System.out.print("   Major  │");
                } else if (i == 13){
                    System.out.print("  Medium  │");
                } else if (i == 8) {
                    System.out.print("   Minor  │");
                } else if (i == 3){
                    System.out.print("No Impact │");
                } else {
                    System.out.print("          │");
                }

                for(int j = INITIAL; j < PROBABILITY_SCALE ;j++){
                    if(i > 20){
                        System.out.print(ANSI_RED_BACKGROUND);
                        printMatrixField(i, j);
                        System.out.print(CYAN_BRIGHT);
                    } else if(i > 15 && j < 73){
                        System.out.print(ANSI_YELLOW_BACKGROUND);
                        printMatrixField(i, j);
                        System.out.print(CYAN_BRIGHT);
                    } else if(i > 15 && j > 72){
                        System.out.print(ANSI_RED_BACKGROUND);
                        printMatrixField(i, j);
                        System.out.print(CYAN_BRIGHT);
                    } else if(i > 10 && j < 25){
                        System.out.print(ANSI_GREEN_BACKGROUND);
                        printMatrixField(i, j);
                        System.out.print(CYAN_BRIGHT);
                    } else if(i > 10 && j < 97){
                        System.out.print(ANSI_YELLOW_BACKGROUND);
                        printMatrixField(i, j);
                        System.out.print(CYAN_BRIGHT);
                    } else if(i > 10 && j > 96){
                        System.out.print(ANSI_RED_BACKGROUND);
                        printMatrixField(i, j);
                        System.out.print(CYAN_BRIGHT);
                    } else if(i > 5 && j < 49){
                        System.out.print(ANSI_GREEN_BACKGROUND);
                        printMatrixField(i, j);
                        System.out.print(CYAN_BRIGHT);
                    } else if(i > 5 && j > 48){
                        System.out.print(ANSI_YELLOW_BACKGROUND);
                        printMatrixField(i, j);
                        System.out.print(CYAN_BRIGHT);
                    } else if (i <= 5){
                        System.out.print(ANSI_GREEN_BACKGROUND);
                        printMatrixField(i, j);
                        System.out.print(CYAN_BRIGHT);
                    }
                }
                sideLegend(i);
            }
            System.out.println();
            System.out.println("           ────────────────────────.───────────────────────.───────────────────────.───────────────────────.───────────────────────.──────>");
            System.out.println("          │     Highly Unlikely    │       Unlikely        │       Possible        │         Likely        │      Very Likely      │       PROBABILITY");
            System.out.println();
            System.out.println();
            System.out.println("                             LEGEND:                ");
            System.out.println("                                " + ANSI_GREEN_BACKGROUND + "        " + CYAN_BRIGHT + " Low Severity (Acceptable)");
            System.out.println("                                " + ANSI_YELLOW_BACKGROUND + "        " + CYAN_BRIGHT + " Medium Severity ");
            System.out.println("                                " + ANSI_RED_BACKGROUND + "        " + CYAN_BRIGHT + " High Severity (Not Acceptable)");
            System.out.println();
            System.out.println("                                        * single Risk plot ");
            System.out.println("                                        ℗ more than one Risk plot ");
            System.out.println("                             Side Legends RiskID: (probability, Impact) ");
        }

    }

    public void printMatrixField(int i, int j){
        int impact, prob;
        int counter = 0;
        ArrayList<Risk> risks = currentProject.getRisks();
        for (Risk risk:risks) {
            impact = (int) Math.round(risk.getImpact() / IMPACT_LIMIT * IMPACT_SCALE);
            if (impact == i){
                prob = (int) Math.round(risk.getProbability() * PROBABILITY_SCALE);
                if (prob == j){
                    counter = counter + 1;
                }
            }
        }

        if(counter == INITIAL){
            System.out.print(" ");
        } else if (counter == FIRST){
            System.out.print(BLACK_BOLD + "*");
        } else {
            System.out.print(BLACK_BOLD + "℗");
        }
    }

    public void sideLegend(int index){
        int impact;
        int counter = 0;
        ArrayList<Risk> risks = currentProject.getRisks();
        for (Risk risk:risks) {
            impact = (int) Math.round(risk.getImpact() / IMPACT_LIMIT * IMPACT_SCALE);
            if (impact == index){
                if(counter == INITIAL){
                    System.out.print("  ID " + risk.getRiskID() + ": (" +
                            risk.getProbability() + ", " + risk.getImpact() + ")");
                }else {
                    System.out.print(" , " + risk.getRiskID() + ": (" +
                            risk.getProbability() + ", " + risk.getImpact() + ")");
                }
                counter = counter + 1;
            }
        }
    }

    public void printSpecificRisk (){
        int INDENT1 = 3;
        int INDENT2 = 2;
        int INDENT3 = 4;
        int INDENT4 = 6;
        int INDENT5 = 10;
        int INDENT6 = 8;
        int INDENT7 = 7;
        int MIN_INDENT = 12;
        String riskID = checkRiskID();
        Risk foundRisk = retrieveRiskByID(riskID, currentProject.getRisks());
        int indent = foundRisk.getRiskName().length() + foundRisk.getRiskID().length() + INDENT1 ;

        if(indent < MIN_INDENT){
            indent = MIN_INDENT;
        }
        int before = (indent - MIN_INDENT)/INDENT1;
        int after = indent - MIN_INDENT - before + INDENT2;
        System.out.println(WHITE_UNDERLINED);
        printEmpty(before);
        System.out.print("NAME OF RISK & (ID)");
        printEmpty(after - INDENT1);
        System.out.print("PROBABILITY");
        printEmpty(INDENT3);
        System.out.print("IMPACT");
        printEmpty(INDENT3);
        System.out.print("RISK");
        printEmpty(INDENT5);
        System.out.print("TYPE");
        printEmpty(MIN_INDENT);
        System.out.print("STATUS    ");
        System.out.println(CYAN_BRIGHT);

        System.out.print(foundRisk.getRiskName() + " (" + foundRisk.getRiskID() + ")");
        int afterName = indent - foundRisk.getRiskName().length();
        printEmpty(afterName);
        printEmpty(INDENT4);
        System.out.print(String.format("%.2f", foundRisk.getProbability()));
        printEmpty(INDENT6);
        System.out.print(String.format("%.1f", foundRisk.getImpact()));
        printEmpty(INDENT4);
        System.out.print(String.format("%.2f", foundRisk.getCalculatedRisk()));
        printEmpty(INDENT6);
        System.out.print(foundRisk.getRiskType());
        printEmpty(INDENT7);
        System.out.print(foundRisk.getRiskStatus());
        System.out.println();
    }

    public void printEmpty(int spaces){
        for(int i = 0; i<spaces; i++){
            System.out.print(" ");
        }
    }

    public void printAllRisks(){
        int INDENT1 = 3;
        int INDENT2 = 2;
        int INDENT3 = 4;
        int INDENT4 = 6;
        int INDENT5 = 8;
        int INDENT6 = 11;
        int MIN_INDENT = 12;
        ArrayList<Risk> risks = currentProject.getRisks();
        if(risks != null){
            int indent = risks.get(INITIAL).getRiskName().length();
            for (Risk currentRisk : risks){
                int newIndent = currentRisk.getRiskName().length() + currentRisk.getRiskID().length() + INDENT1;
                if   ( newIndent > indent) {
                    indent = newIndent;
                }
            }

            if(indent < MIN_INDENT){
                indent = MIN_INDENT;
            }
            int before = (indent - MIN_INDENT)/INDENT1;
            int after = indent - MIN_INDENT - before + INDENT2;
            System.out.println(WHITE_UNDERLINED);
            printEmpty(before);
            System.out.print("NAME OF RISK & (ID)");
            printEmpty(after - INDENT1);
            System.out.print("PROBABILITY");
            printEmpty(INDENT3);
            System.out.print("IMPACT");
            printEmpty(INDENT3);
            System.out.print("RISK");
            printEmpty(INDENT5);
            System.out.print("TYPE");
            printEmpty(INDENT6);
            System.out.print("STATUS    ");
            System.out.println(CYAN_BRIGHT);

            for ( Risk foundRisk : risks) {
                System.out.print(foundRisk.getRiskName() + " (" + foundRisk.getRiskID() + ")");
                int afterName = indent - foundRisk.getRiskName().length();
                printEmpty(afterName);
                printEmpty(INDENT4);
                System.out.print(String.format("%.2f", foundRisk.getProbability()));
                printEmpty(INDENT5);
                System.out.print(String.format("%.1f", foundRisk.getImpact()));
                printEmpty(INDENT4);
                System.out.print(String.format("%.2f", foundRisk.getCalculatedRisk()));
                printEmpty(INDENT4);
                System.out.print(foundRisk.getRiskType());
                printEmpty(INDENT4);
                System.out.print(foundRisk.getRiskStatus());
                System.out.println();
            }
        } else {
            System.out.println("There is no registered risk.");
        }
    }

    public void printNumberOfRisk(){
        int numberOfRisks;
        if(currentProject != null){
            ArrayList<Risk> risks = currentProject.getRisks();
            if (risks!=null){
                numberOfRisks = risks.size();
                if (numberOfRisks == FIRST){
                    System.out.println("*******************************");
                    System.out.println("There is " + risks.size() + " registered risk.");
                } else {
                    System.out.println("*******************************");
                    System.out.println("There are " + risks.size() + " registered risks.");
                }
            } else {
                System.out.println("There is no registered risk.");
            }
        }
    }

    public void percentageConverter(){ // this methods handles the converting to percentage function.
        final int PROBABILITY = 1;
        final int IMPACT = 2;
        final int QUIT = 3;
        int option ;
        if(currentProject != null){
            ArrayList<Risk> risks = currentProject.getRisks();
            if(risks != null){
                do {
                    System.out.println("1. CONVERT PROBABILITY");
                    System.out.println("2. CONVERT IMPACT.");
                    System.out.println("3. QUIT");
                    option = new KeyboardInput().positiveInt();

                    switch (option) {
                        case PROBABILITY:
                            probabilityPercentage();
                            System.out.println("*************************************");
                            break;

                        case IMPACT:
                            impactPercentage();
                            System.out.println("*************************************");
                            break;

                        case QUIT:
                            System.out.println("*************************************");
                            break;

                        default:
                            System.out.println("Enter a valid input");
                            break;
                    }
                } while (option != QUIT);
            }else {
                System.out.println("There are no registered risks ");
            }
        }
    }

    public void probabilityPercentage(){ // this method coverts probability value of a given risk to percentage value.

        final double fixedProbability = 10.0;
        double maxPercentage = 100.0;
        if(currentProject != null){
            String riskID = checkRiskID();
            Risk foundRisk = retrieveRiskByID(riskID, currentProject.getRisks());

            if(foundRisk != null){
                double foundProbability = foundRisk.getProbability();
                double convertedProbability = (foundProbability/fixedProbability) * maxPercentage;
                System.out.println("The risk " + foundRisk.getRiskName() + " has a probability of " + convertedProbability
                        + " %");
            } else{
                System.out.println("The risk name does not exist.");
            }
        }
    }

    public void impactPercentage(){ // this method converts impact values of a given risk to percentage values.
        final double fixedImpact = 10.0;
        double maxPercentage = 100.0;
        if(currentProject != null){
            String riskID = checkRiskID();
            Risk foundRisk = retrieveRiskByID(riskID, currentProject.getRisks());
            if (foundRisk != null){
                double foundImpact = foundRisk.getImpact();
                double convertedImpact = (foundImpact/fixedImpact) * maxPercentage;
                System.out.println("The risk "+ foundRisk.getRiskName() + " has an impact of "+ convertedImpact + " %");
            } else {
                System.out.println("The risk name does not exist");
            }
        }
    }

}