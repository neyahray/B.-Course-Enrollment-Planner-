import java.lang.reflect.Array;
import java.util.*;

public class Main {
    static HashMap<String, HashSet<String>> prereqsMap = new HashMap<>();
    static HashMap<String, HashSet<String>> completedMap = new HashMap<>();

    public static void toPrint() {
        System.out.println("> ADD_COURSE (enter only course(s))");
        System.out.println("> ADD_PREREQ (enter course and prereq(s))");
        System.out.println("> PREREQS (enter course)");
        System.out.println("> COMPLETE (enter student and course(s))");
        System.out.println("> DONE (enter student(s))");
        System.out.println("> CAN_TAKE (enter student and course(s))");
        System.out.println("> EXIT");
    }

    public static String ADD_COURSE(ArrayList<String> courseName) {
        for (String n : courseName) {
            prereqsMap.putIfAbsent(n, new HashSet<>());
        }
        return  "* Course(s) added: " + String.join(", ", courseName) + " *" + "\n";
    }

    public static String ADD_PREREQ(String course, ArrayList<String> prereq) {
        prereqsMap.putIfAbsent(course, new HashSet<>());
        String str = "";
        for (String p : prereq) {
            if (course.equals(p)) {
                str = "* A course (" + course + ") can't be it's own prerequisite (" + p + "), action rejected, try again";
            } else {
                prereqsMap.get(course).add(p);
                str = "* Course and prereq for course added: " + course + "-> " + String.join(", ", prereq) + " *";
            }

        }
        return str;
    }

    public static String PREREQS(ArrayList<String> courseName) {
        String str = "";
        for (String c : courseName) {
            if (!prereqsMap.containsKey(c)) {
                str += "* There is no such that course *" + "\n";
            } else if (prereqsMap.get(c).isEmpty()) {
                str += "* Course: " + c + ", has no any prereqs *" + "\n";
            } else {
                str += "* Course: " + c + ", prereqs: " + String.join(", ", prereqsMap.get(c)) + " *" + "\n";
            }
        }
        return str;
    }

    public static String COMPLETE(String student, ArrayList<String> courses) {
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

    public static String DONE(ArrayList<String> students) {
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

    public static String CAN_TAKE(String student, ArrayList<String> courses) {
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
    //MAIN
    public static void main(String[] args) {
        prereqsMap.putIfAbsent("Calculus2", new HashSet<>(Arrays.asList("Calculus1", "Algebra")));
        prereqsMap.putIfAbsent("Programming Language2", new HashSet<>(Arrays.asList("Programming Language1")));

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("--HELP--");
            System.out.println("--EXIT--");
            System.out.println("Help or Exit?: ");
            String choice = scanner.next().toLowerCase();
            scanner.nextLine();
            if (choice.equals("help")) {
                inner:
                while (true) {
                    toPrint();
                    System.out.println("Enter an option: ");
                    String option = scanner.nextLine();
                    ArrayList<String> optionArray = new ArrayList<>(Arrays.asList(option.trim().split("\\s+")));
                    switch (optionArray.get(0)) {
                        case "ADD_COURSE":
                            if (optionArray.size() > 1) {
                                ArrayList<String> courseNames = new ArrayList<>(optionArray.subList(1, optionArray.size()));
                                System.out.println(ADD_COURSE(courseNames));
                            } else {
                                System.out.println("Please enter a course name(s): ");
                                String courseName = scanner.nextLine();
                                ArrayList<String> courseNameArray = new ArrayList<>(Arrays.asList(courseName.trim().split("\\s+")));
                                System.out.println(ADD_COURSE(courseNameArray));
                            }
                            break;
                        case "ADD_PREREQ":
                            if (optionArray.size() > 2) {
                                ArrayList<String> prereqNames = new ArrayList<>(optionArray.subList(2, optionArray.size()));
                                System.out.println(ADD_PREREQ(optionArray.get(1), prereqNames));
                            } else if (optionArray.size() == 2) {
                                scanner.nextLine();
                                String prereqName = scanner.nextLine();
                                ArrayList<String> prereqNameArray = new ArrayList<>(Arrays.asList(prereqName.trim().split("\\s+")));
                                System.out.println(ADD_PREREQ(optionArray.get(1), prereqNameArray));
                            } else {
                                System.out.println("Please enter a course name: ");
                                String courseName = scanner.nextLine();
                                System.out.println("Please enter a prereq name(s): ");
                                String prereqName = scanner.nextLine();
                                ArrayList<String> prereqNameArray = new ArrayList<>(Arrays.asList(prereqName.trim().split("\\s+")));
                                System.out.println(ADD_PREREQ(courseName, prereqNameArray));
                            }
                            break;
                        case "PREREQS":
                            if (optionArray.size() > 1) {
                                ArrayList<String> coursesNames = new ArrayList<>(optionArray.subList(1, optionArray.size()));
                                System.out.println(PREREQS(coursesNames));
                            } else {
                                System.out.println("Please enter a course name: ");
                                String coursesName = scanner.nextLine();
                                ArrayList<String> coursesNamesArray = new ArrayList<>(Arrays.asList(coursesName.trim().split("\\s+")));
                                System.out.println(PREREQS(coursesNamesArray));
                            }
                            break;
                        case "COMPLETE":
                            if (optionArray.size() > 2) {
                                ArrayList<String> courses = new ArrayList<>(optionArray.subList(2, optionArray.size()));
                                System.out.println(COMPLETE(optionArray.get(1), courses));
                            } else if (optionArray.size() == 2) {
                                System.out.println("Please enter a course name(s): ");
                                scanner.nextLine();
                                String courseName = scanner.nextLine();
                                ArrayList<String> courseNameArray = new ArrayList<>(Arrays.asList(courseName.trim().split("\\s+")));
                                System.out.println(COMPLETE(optionArray.get(1), courseNameArray));
                            } else {
                                System.out.println("Please enter a student's name: ");
                                String student = scanner.nextLine();
                                System.out.println("Please enter a course name(s): ");
                                String course = scanner.nextLine();
                                ArrayList<String> courseArray = new ArrayList<>(Arrays.asList(course.trim().split("\\s+")));
                                System.out.println(COMPLETE(student, courseArray));
                            }
                            break;
                        case "DONE":
                            if (optionArray.size() > 1) {
                                ArrayList<String> course = new ArrayList<>(optionArray.subList(1, optionArray.size()));
                                System.out.println(DONE(course));
                            } else {
                                System.out.println("Please enter student's name(s): ");
                                String studentName = scanner.nextLine();
                                ArrayList<String> studentNameArray = new ArrayList<>(Arrays.asList(studentName.trim().split("\\s+")));
                                System.out.println(DONE(studentNameArray));
                            }
                            break;
                        case "CAN_TAKE":
                            if (optionArray.size() > 2) {
                                ArrayList<String> courses = new ArrayList<>(optionArray.subList(2, optionArray.size()));
                                System.out.println(CAN_TAKE(optionArray.get(1), courses));
                            } else if (optionArray.size() == 2) {
                                System.out.println("Please enter course name(s): ");
                                String course = scanner.nextLine();
                                ArrayList<String> courseArray = new ArrayList<>(Arrays.asList(course.trim().split("\\s+")));
                                System.out.println(CAN_TAKE(optionArray.get(1), courseArray));
                            } else {
                                System.out.println("Please enter student's name: ");
                                String student = scanner.nextLine();
                                System.out.println("Please enter course name(s): ");
                                String course = scanner.nextLine();
                                ArrayList<String> courseArray = new ArrayList<>(Arrays.asList(course.trim().split("\\s+")));
                                System.out.println(CAN_TAKE(student, courseArray));
                            }
                            break;
                        case "EXIT":
                            System.out.println("You exited from menu");
                            break inner;
                    }
                }
            } else if (choice.equals("exit")) {
                System.out.println("You exited from menu");
                break;
            }
        }
    }
}