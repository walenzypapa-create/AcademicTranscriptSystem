
package utils;

import models.Student;
import models.Course;
import models.Transcript;
import java.io.*;
        import java.util.List;

public class DatabaseUtils {
    private static final String DATA_DIR = "data";
    private static final String STUDENTS_FILE = "students.ser";
    private static final String COURSES_FILE = "courses.ser";

    static {
        // Create data directory if it doesn't exist
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void saveStudents(List<Student> students) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_DIR + File.separator + STUDENTS_FILE))) {
            oos.writeObject(students);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Student> loadStudents() throws IOException, ClassNotFoundException {
        File file = new File(DATA_DIR + File.separator + STUDENTS_FILE);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (List<Student>) ois.readObject();
        }
    }

    public static void saveCourses(List<Course> courses) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_DIR + File.separator + COURSES_FILE))) {
            oos.writeObject(courses);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Course> loadCourses() throws IOException, ClassNotFoundException {
        File file = new File(DATA_DIR + File.separator + COURSES_FILE);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (List<Course>) ois.readObject();
        }
    }
}
