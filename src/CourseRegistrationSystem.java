import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Student {
    private String studentID;
    private String name;

    public Student(String studentID, String name) {
        this.studentID = studentID;
        this.name = name;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getName() {
        return name;
    }

    public void displayStudentInfo() {
        System.out.println("Student ID: " + studentID + ", Name: " + name);
    }
}

class Course {
    private String courseName;
    private int capacity;
    private List<Student> enrolledStudents;

    public Course(String courseName, int capacity) {
        this.courseName = courseName;
        this.capacity = capacity;
        this.enrolledStudents = new ArrayList<>();
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public boolean isFull() {
        return enrolledStudents.size() >= capacity;
    }

    public boolean isStudentRegistered(String studentID) {
        for (Student s : enrolledStudents) {
            if (s.getStudentID().equals(studentID)) {
                return true;
            }
        }
        return false;
    }

    public void addStudent(Student student) {
        enrolledStudents.add(student);
    }

    public void displayCourseInfo() {
        System.out.println("Course Name: " + courseName);
        System.out.println("Capacity: " + capacity);
        System.out.println("Enrolled Students Count: " + enrolledStudents.size());
        System.out.println("Enrolled Students:");
        if (enrolledStudents.isEmpty()) {
            System.out.println("  - No enrolled students yet.");
        } else {
            for (Student s : enrolledStudents) {
                System.out.print("  - ");
                s.displayStudentInfo();
            }
        }
        System.out.println("----------------------------------------");
    }
}

public class CourseRegistrationSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Course> courses = new ArrayList<>();

        System.out.println("=== COURSE REGISTRATION SYSTEM SETUP ===");
        int courseCount = 0;

        while (courseCount < 3) {
            System.out.print("How many courses do you want to create? (At least 3): ");
            if (scanner.hasNextInt()) {
                courseCount = scanner.nextInt();
                scanner.nextLine();
                if (courseCount < 3) {
                    System.out.println("Error: You must create at least 3 courses!");
                }
            } else {
                System.out.println("Please enter a valid number.");
                scanner.next();
            }
        }

        for (int i = 1; i <= courseCount; i++) {
            System.out.println("\n--- Course " + i + " Details ---");
            System.out.print("Course Name: ");
            String cName = scanner.nextLine();
            System.out.print("Classroom Capacity: ");
            int cCapacity = scanner.nextInt();
            scanner.nextLine();

            Course course = new Course(cName, cCapacity);
            courses.add(course);
        }

        System.out.println("\nAvailable Courses for Registration:");
        for (Course c : courses) {
            System.out.println("- " + c.getCourseName() + " (Capacity: " + c.getCapacity() + ")");
        }

        boolean running = true;
        while (running) {
            System.out.println("\n=== OPERATION MENU ===");
            System.out.println("1. Register Student");
            System.out.println("2. View All Courses");
            System.out.println("3. Exit");
            System.out.print("Your choice (1-3): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":

                    System.out.print("Student ID: ");
                    String sID = scanner.nextLine();
                    System.out.print("Student Name: ");
                    String sName = scanner.nextLine();
                    System.out.print("Course Name to register for: ");
                    String targetCourseName = scanner.nextLine();

                    Course targetCourse = null;
                    for (Course c : courses) {
                        if (c.getCourseName().equalsIgnoreCase(targetCourseName)) {
                            targetCourse = c;
                            break;
                        }
                    }

                    if (targetCourse == null) {
                        System.out.println("No course found with that name.");
                    } else if (targetCourse.isFull()) {
                        System.out.println("This course is full. Registration failed.");
                    } else if (targetCourse.isStudentRegistered(sID)) {
                        System.out.println("This student is already registered for this course.");
                    } else {
                        Student newStudent = new Student(sID, sName);
                        targetCourse.addStudent(newStudent);
                        System.out.println("Registration successful.");
                    }
                    break;

                case "2":
                    System.out.println("\n=== ALL COURSES AND ENROLLED STUDENTS ===");
                    for (Course c : courses) {
                        c.displayCourseInfo();
                    }
                    break;

                case "3":
                    running = false;
                    System.out.println("Exiting the program. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice, please enter 1, 2, or 3.");
            }
        }
        scanner.close();
    }
}