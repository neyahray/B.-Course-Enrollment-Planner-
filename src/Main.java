import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static HashMap<String, HashSet<String>> prereqsMap = new HashMap<>();
    static HashMap<String, HashSet<String>> completedMap = new HashMap<>();

    public static void toPrint() {
        System.out.println("> ADD_COURSE (enter course(s))");
        System.out.println("> ADD_PREREQ (enter course and prereq(s))");
        System.out.println("> PREREQS (enter course)");
        System.out.println("> COMPLETE (enter student and course(s))");
        System.out.println("> DONE (enter student(s))");
        System.out.println("> CAN_TAKE (enter student and course(s))");
        System.out.println("> EXIT");
    }

    public static boolean isValid(String name) {
        return name.matches("[a-zA-Z0-9_]+");
    }

    public static boolean validateArray(ArrayList<String> input) {
        for (String s : input) {
            if (!isValid(s)) {
                System.out.println("* Invalid name: " + s + ", use only letters/digits/underscore *");
                return false;
            }
        }
        return true;
    }

    public static HashSet<String> toLowerCaseSet(List<String> input) {
        HashSet<String> result = new HashSet<>();
        for (String s : input) {
            result.add(s.toLowerCase());
        }
        return result;
    }

    public static ArrayList<String> getInput() {
        String input = scanner.nextLine();
        return new ArrayList<>(Arrays.asList(input.trim().split("\\s+")));
    }

    public static String promptOne(String message) {
        while (true) {
            System.out.println(message);
            String input = getInput().get(0);
            if (isValid(input)) {
                return input.toLowerCase();
            }
            System.out.println("* Invalid name: " + input + ", use only letters/digits/underscore *");
        }
    }

    public static HashSet<String> promptSet(String message) {
        while (true) {
            System.out.println(message);
            ArrayList<String> input = getInput();
            boolean valid = true;
            for (String s : input) {
                if (!isValid(s)) {
                    System.out.println("* Invalid name: " + s + ", use only letters/digits/underscore *");
                    valid = false;
                    break;
                }
            }
            if (valid) {
                HashSet<String> result = new HashSet<>();
                for (String s : input) {
                    result.add(s.toLowerCase());
                }
                return result;
            }
        }
    }

    public static String ADD_COURSE(HashSet<String> course) {
        for (String c : course) {
            prereqsMap.putIfAbsent(c, new HashSet<>());
        }
        String str = "";
        if (course.size() == 1) {
            str =  "* Course added: " + String.join(", ", course) + " *";
        } else {
            str =  "* Courses added: " + String.join(", ", course) + " *";
        }
        return str;
    }

    public static String ADD_PREREQ(String course, HashSet<String> prereq) {
        prereqsMap.putIfAbsent(course, new HashSet<>());
        if (prereq.contains(course)) {
            return "* A course: " + course + " can't be its own prerequisite, action rejected, try again with no duplicate *";
        }
        for (String p : prereq) {
            prereqsMap.putIfAbsent(p, new HashSet<>());
            prereqsMap.get(course).add(p);
        }
        String str = "";
        if (prereq.size() == 1) {
            str = "* Course and prereq added: " + course + " -> " + String.join(", ", prereq) + " *";
        } else {
            str = "* Course and prereqs added: " + course + " -> " + String.join(", ", prereq) + " *";
        }
        return str;
    }

    public static String PREREQS(HashSet<String> course) {
        String str = "";
        for (String c : course) {
            if (!prereqsMap.containsKey(c)) {
                str += "* There is no such that course as: " + c + " *" + "\n";
            } else if (prereqsMap.get(c).isEmpty()) {
                str += "* Course: " + c + ", has no any prereqs *" + "\n";
            } else {
                str += "* Course: " + c + ", prereqs: " + String.join(", ", prereqsMap.get(c)) + " *" + "\n";
            }
        }
        return str;
    }

    public static String COMPLETE(String student, HashSet<String> courses) {
        String str = "";
        completedMap.putIfAbsent(student, new HashSet<>());
        for (String c : courses) {
            completedMap.get(student).add(c);
        }

        if (courses.size() > 1) {
            str = "* " + student + " completed courses: " + String.join(", ", courses) + " *";
        } else {
            str = "* " + student + " completed course: " + String.join(", ", courses) + " *";
        }
        return str;
    }

    public static String DONE(HashSet<String> students) {
        String str = "";
        for (String s : students) {
            if (!completedMap.containsKey(s)) {
                str += "* No records about student " + s + " *" + "\n";
            } else if (completedMap.get(s).isEmpty()) {
                str += "* Student " + s + " hasn't done any courses *" + "\n";
            } else {
                if (completedMap.get(s).size() > 1) {
                    str += "* Student " + s + " has done courses: " + String.join(", ", completedMap.get(s)) + " *" + "\n";
                } else {
                    str += "* Student " + s + " has done course: " + String.join(", ", completedMap.get(s)) + " *" + "\n";
                }
            }
        }
        return str;
    }

    public static String CAN_TAKE(String student, HashSet<String> courses) {
        String str = "";
        for (String c : courses) {
            if (!prereqsMap.containsKey(c) || prereqsMap.get(c).isEmpty()) {
                str += "* Yes! Student " + student + " can take this course *" + "\n";
            } else if (!completedMap.containsKey(student) || !completedMap.get(student).containsAll(prereqsMap.get(c))) {
                str += "* No! Student " + student + " can't take this course *" + "\n";
            } else {
                str += "* Yes! Student " + student + " can take this course *" + "\n";
            }
        }
        return str;
    }

//    public static String CAN_TAKE_EXPERIMENT(String student, ArrayList<String> courses) {
//        String str = "";
//        for (String c : courses) {
//            if (!prereqsMap.containsKey(c) || prereqsMap.get(c).isEmpty()) {
//                str += "* Yes! Student " + student + " can take this course *" + "\n";
//            } else {
//                ArrayList<String> prereqList = new ArrayList<>(prereqsMap.get(c));
//                ArrayList<String> completedList = new ArrayList<>();
//
//                if (completedMap.containsKey(student)) {
//                    completedList = new ArrayList<>(completedMap.get(student));
//                }
//
//                if (completedList.containsAll(prereqList)) {
//                    str += "* Yes! Student " + student + " can take this course *" + "\n";
//                } else {
//                    str += "* No! Student " + student + " can't take this course *" + "\n";
//                }
//            }
//        }
//        return str;
//    }
//
//    public static void timingExperiment() {
//        System.out.println("=== Timing Experiment ===");
//        int[] sizes = {1, 5, 10, 50, 100};
//        for (int i = 0; i < 100000; i++) {
//            CAN_TAKE("TestStudent", new HashSet<>(Arrays.asList("TestCourse")));
//        }
//        for (int size : sizes) {
//            HashSet<String> prereqs = new HashSet<>();
//            HashSet<String> completed = new HashSet<>();
//            for (int i = 0; i < size; i++) {
//                prereqs.add("Course" + i);
//                completed.add("Course" + i);
//            }
//
//            prereqsMap.put("TestCourse", prereqs);
//            completedMap.put("TestStudent", completed);
//
//
//            long totalHashSet = 0;
//            for (int run = 0; run < 10; run++) {
//                long start = System.nanoTime();
//                CAN_TAKE("TestStudent", new HashSet<>(Arrays.asList("TestCourse")));
//                long end = System.nanoTime();
//                totalHashSet += (end - start);
//            }
//
//            long totalArrayList = 0;
//            for (int run = 0; run < 10; run++) {
//                long start = System.nanoTime();
//                CAN_TAKE_EXPERIMENT("TestStudent", new ArrayList<>(Arrays.asList("TestCourse")));
//                long end = System.nanoTime();
//                totalArrayList += (end - start);
//            }
//            System.out.println("Prereqs: " + size
//                    + " | HashSet avg: " + (totalHashSet / 10) + "\n"
//                    + " | ArrayList avg: " + (totalArrayList / 10) + "\n");
//        }
//    }

    //MAIN
    public static void main(String[] args) {
        prereqsMap.putIfAbsent("calculus2", new HashSet<>(Arrays.asList("calculus1", "algebra")));
        prereqsMap.putIfAbsent("programming_language2", new HashSet<>(Arrays.asList("programming_language1")));

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("--HELP--");
            System.out.println("--EXIT--");
            System.out.println("Help or Exit?: ");
            String choice = scanner.next().toLowerCase();
            scanner.nextLine();
            if (choice.equals("help")) {
                toPrint();
                inner:
                while (true) {
                    System.out.println("Enter an option: ");
                    String option = scanner.nextLine();
                    ArrayList<String> optionArray = new ArrayList<>(Arrays.asList(option.trim().split("\\s+")));
                    switch (optionArray.get(0).toUpperCase()) {
                        case "ADD_COURSE":
                            if (optionArray.size() > 1) {
                                if (validateArray(new ArrayList<>(optionArray.subList(1, optionArray.size())))) {
                                    System.out.println(ADD_COURSE(toLowerCaseSet(optionArray.subList(1, optionArray.size()))));
                                }
                            } else {
                                System.out.println(ADD_COURSE(promptSet("Please enter a course name(s): ")));
                            }
                            break;
                        case "ADD_PREREQ":
                            if (optionArray.size() > 2) {
                                if (validateArray(new ArrayList<>(optionArray.subList(1, optionArray.size())))) {
                                    System.out.println(ADD_PREREQ(optionArray.get(1).toLowerCase(), toLowerCaseSet(optionArray.subList(2, optionArray.size()))));
                                }
                            } else if (optionArray.size() == 2) {
                                if (isValid(optionArray.get(1))) {
                                    System.out.println(ADD_PREREQ(optionArray.get(1).toLowerCase(), promptSet("Please enter a prereq name(s): ")));
                                } else {
                                    System.out.println("* Invalid name: " + optionArray.get(1) + ", use only letters/digits/underscore *");
                                }
                            } else {
                                String courseName = promptOne("Please enter a course name: ");
                                System.out.println(ADD_PREREQ(courseName, promptSet("Please enter a prereq name(s): ")));
                            }
                            break;
                        case "PREREQS":
                            if (optionArray.size() > 1) {
                                if (validateArray(new ArrayList<>(optionArray.subList(1, optionArray.size())))) {
                                    System.out.println(PREREQS(toLowerCaseSet(optionArray.subList(1, optionArray.size()))));
                                }
                            } else {
                                System.out.println(PREREQS(promptSet("Please enter a course name: ")));
                            }
                            break;
                        case "COMPLETE":
                            if (optionArray.size() > 2) {
                                if (validateArray(new ArrayList<>(optionArray.subList(1, optionArray.size())))) {
                                    System.out.println(COMPLETE(optionArray.get(1).toLowerCase(), toLowerCaseSet(optionArray.subList(2, optionArray.size()))));
                                }
                            } else if (optionArray.size() == 2) {
                                if (isValid(optionArray.get(1))) {
                                    System.out.println(COMPLETE(optionArray.get(1).toLowerCase(), promptSet("Please enter a course name(s): ")));
                                } else {
                                    System.out.println("* Invalid name: " + optionArray.get(1) + ", use only letters/digits/underscore *");
                                }
                            } else {
                                String student = promptOne("Please enter a student's name: ");
                                System.out.println(COMPLETE(student, promptSet("Please enter a course name(s): ")));
                            }
                            break;
                        case "DONE":
                            if (optionArray.size() > 1) {
                                if (validateArray(new ArrayList<>(optionArray.subList(1, optionArray.size())))) {
                                    System.out.println(DONE(toLowerCaseSet(optionArray.subList(1, optionArray.size()))));
                                }
                            } else {
                                System.out.println(DONE(promptSet("Please enter student's name(s): ")));
                            }
                            break;
                        case "CAN_TAKE":
                            if (optionArray.size() > 2) {
                                if (validateArray(new ArrayList<>(optionArray.subList(1, optionArray.size())))) {
                                    System.out.println(CAN_TAKE(optionArray.get(1).toLowerCase(), toLowerCaseSet(optionArray.subList(2, optionArray.size()))));
                                }
                            } else if (optionArray.size() == 2) {
                                if (isValid(optionArray.get(1))) {
                                    System.out.println(CAN_TAKE(optionArray.get(1).toLowerCase(), promptSet("Please enter a course name(s): ")));
                                } else {
                                    System.out.println("* Invalid name: " + optionArray.get(1) + ", use only letters/digits/underscore *");
                                }
                            } else {
                                String student = promptOne("Please enter student's name: ");
                                System.out.println(CAN_TAKE(student, promptSet("Please enter course name(s): ")));
                            }
                            break;
                        case "EXIT":
                            System.out.println("You exited from option-menu");
                            break inner;
                        default:
                            System.out.println("* Unknown command, try again *");
                            break;
                    }
                }
            } else if (choice.equals("exit")) {
                System.out.println("You exited from menu");
                break;
            }
        }
        //timingExperiment();
    }
}