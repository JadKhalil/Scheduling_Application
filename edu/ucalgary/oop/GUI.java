package edu.ucalgary.oop;


import javax.swing.*;
import java.awt.*;
import java.util.*;


/**
 * GUI is a class which creates the GUI of the program.
 *
 * It contains information about the task which are provided in the database
 * @author     Edward An, Karam Baroud, Evan Barker, Jad Khalil
 * @version    1.7
 * @since      1.0
 */
public class GUI {
        private static ArrayList<Integer> emptyTimeSlotsArray = new ArrayList<>();
        private static JFrame printScheduleFrame = new JFrame();
        private static JFrame selectTaskFrame = new JFrame();
        private static JFrame volunteerOrRescheduleFrame = new JFrame();
        private static JFrame selectHoursFrame = new JFrame();
        private static JFrame addVolunteersFrame = new JFrame();


        /**
         * The main method
         * Reads from database then starts the GUI
         */
        public static void main(String[] args) {
                Schedule.populateTreeMap();
                EventQueue.invokeLater(() -> {
                        startGUI();
                });
        }

        /**
         * Starting point for the GUI.
         * Creates one of two frames based on whether the schedule is valid or not
         */
        public static void startGUI() {
                boolean isValid = Schedule.isValidSchedule();
                if (isValid) {
                        printScheduleFrame = createPrintScheduleFrame();
                        printScheduleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        printScheduleFrame.setVisible(true);
                }
                else {
                        volunteerOrRescheduleFrame = createVolunteerOrRescheduleFrame();
                        volunteerOrRescheduleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        volunteerOrRescheduleFrame.setVisible(true);
                }
        }

        /**
         * This method creates a frame for adding volunteers to the schedule
         * @return addVolunteerFrame
         */
        public static JFrame createAddVolunteersFrame() {
                JFrame frame = new JFrame();
                frame.setSize(800, 500);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel titlePanel = new JPanel(new FlowLayout());
                JLabel title = new JLabel("Select the start hour you would like to assign extra volunteers to");
                titlePanel.add(title);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

                Schedule.getSchedule().forEach((startHour, tasksArrayList)-> {
                        int totalTime=0;
                        for (Task task: tasksArrayList) {
                                totalTime += task.getDuration() + task.getPrepTime();
                        }
                        if ((totalTime > 60 && tasksArrayList.get(0).getExtraVolunteerStatus() == false)) {
                                JButton button = new JButton(startHour.toString() + ":00");
                                button.addActionListener(e -> {
                                        Schedule.addBackupVolunteer(startHour);
                                        JOptionPane.showMessageDialog(null, "Extra volunteer was assigned for hour " + startHour.toString());
                                        if (Schedule.isValidSchedule()) {
                                                printScheduleFrame = createPrintScheduleFrame();
                                                addVolunteersFrame.dispose();
                                                printScheduleFrame.setVisible(true);
                                        }
                                        else {
                                                volunteerOrRescheduleFrame = createVolunteerOrRescheduleFrame();
                                                addVolunteersFrame.dispose();
                                                volunteerOrRescheduleFrame.setVisible(true);
                                        }
                                });
                                buttonPanel.add(button);
                        }

                });
                JButton backButton = new JButton("Back to Menu");
                backButton.addActionListener(e -> {
                        volunteerOrRescheduleFrame = createVolunteerOrRescheduleFrame();
                        addVolunteersFrame.dispose();
                        volunteerOrRescheduleFrame.setVisible(true);
                });
                buttonPanel.add(backButton);
                frame.getContentPane().add(titlePanel, BorderLayout.PAGE_START);
                frame.getContentPane().add(buttonPanel, BorderLayout.CENTER);
                return frame;
        }

        /**
         * This method creates a frame for selecting an hour the selected task can be moved to.
         * It utilizes a method from the Schedule class for the logic.
         * @param oldStartHour
         * @param taskIndex
         * @return selectHoursFrame
         */
        public static JFrame createSelectHoursFrame(int oldStartHour, int taskIndex) {
                JFrame frame = new JFrame();
                frame.setSize(800, 500);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel titlePanel = new JPanel(new FlowLayout());
                JLabel title = new JLabel("Select the hours you would like to move the task to");
                titlePanel.add(title);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

                for (int i=0; i<emptyTimeSlotsArray.size(); i++) {
                        int newStartHour = emptyTimeSlotsArray.get(i);
                        JButton button = new JButton("Hour: " + newStartHour);
                        button.addActionListener(e -> {
                                Schedule.modifyStartHour(oldStartHour, taskIndex, newStartHour);
                                if (Schedule.isValidSchedule()) {
                                        printScheduleFrame = createPrintScheduleFrame();
                                        selectHoursFrame.dispose();
                                        printScheduleFrame.setVisible(true);
                                }
                                else {
                                        volunteerOrRescheduleFrame = createVolunteerOrRescheduleFrame();
                                        selectHoursFrame.dispose();
                                        volunteerOrRescheduleFrame.setVisible(true);
                                }
                        });
                        buttonPanel.add(button);
                }
                JButton backButton = new JButton("Back to Menu");
                backButton.addActionListener(e -> {
                        volunteerOrRescheduleFrame = createVolunteerOrRescheduleFrame();
                        selectHoursFrame.dispose();
                        volunteerOrRescheduleFrame.setVisible(true);
                });
                buttonPanel.add(backButton);
                frame.getContentPane().add(titlePanel, BorderLayout.PAGE_START);
                frame.getContentPane().add(buttonPanel, BorderLayout.CENTER);
                return frame;
        }

        /**
         * This method creates a frame for selecting a task to move.
         * It utilizes a method from the Schedule class for the logic.
         * @return selectTaskFrame
         */
        public static JFrame createSelectTaskFrame() {
                JFrame frame = new JFrame();
                frame.setSize(800, 500);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel titlePanel = new JPanel(new FlowLayout());
                JLabel title = new JLabel("Select one of the tasks to move");
                titlePanel.add(title);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
                TreeMap<Integer, ArrayList<Integer>> indexMap = Schedule.showTasksToBeMoved();

                for (Map.Entry<Integer, ArrayList<Integer>> entry : indexMap.entrySet()) {
                        Integer startHour = entry.getKey();
                        ArrayList<Integer> indexList = entry.getValue();

                        for (Integer index: indexList)
                        {
                                Task taskObject = Schedule.getSchedule().get(startHour).get(index);
                                JButton button = new JButton(startHour.toString() + ":00 " +
                                        taskObject.getTaskType() + " - " + taskObject.getAnimal().getSpecies() + " (" + taskObject.getAnimal().getName() + ") " + taskObject.getDuration() + " minutes");
                                button.addActionListener(e -> {
                                        emptyTimeSlotsArray = Schedule.showEmptyTimeSlots(startHour, index);
                                        selectHoursFrame = createSelectHoursFrame(startHour, index);
                                        selectTaskFrame.dispose();
                                        selectHoursFrame.setVisible(true);
                                });
                                buttonPanel.add(button);
                        }
                }
                JButton button = new JButton("Back to Menu");
                button.addActionListener(e -> {
                        volunteerOrRescheduleFrame = createVolunteerOrRescheduleFrame();
                        selectTaskFrame.dispose();
                        volunteerOrRescheduleFrame.setVisible(true);
                });
                buttonPanel.add(button);
                frame.getContentPane().add(titlePanel, BorderLayout.PAGE_START);
                frame.getContentPane().add(buttonPanel, BorderLayout.CENTER);
                return frame;

        }

        /**
         * This method creates a menu frame where the user gets to choose whether they want to
         * add a volunteer or reschedule
         * @return volunteerOrRescheduleFrame
         */
        public static JFrame createVolunteerOrRescheduleFrame() {
                JFrame frame = new JFrame();
                frame.setSize(800, 500);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel titlePanel = new JPanel(new FlowLayout());
                JPanel buttonPanel = new JPanel(new FlowLayout());
                JLabel title = new JLabel("Your current schedule requires you to either call backup volunteers or reschedule tasks \n"
                        + "Please choose one of the following:");
                titlePanel.add(title);

                JButton volunteerButton = new JButton("Add Backup Volunteer(s)");
                volunteerButton.addActionListener(e -> {
                        addVolunteersFrame = createAddVolunteersFrame();
                        volunteerOrRescheduleFrame.dispose();
                        addVolunteersFrame.setVisible(true);
                });

                JButton rescheduleButton = new JButton("Reschedule");
                rescheduleButton.addActionListener(e -> {
                        selectTaskFrame = createSelectTaskFrame();
                        volunteerOrRescheduleFrame.dispose();
                        selectTaskFrame.setVisible(true);
                });

                JButton createSchedule = new JButton("Create schedule anyways");
                createSchedule.addActionListener(e -> {
                        int userChoice = JOptionPane.showConfirmDialog(createSchedule,"Are you sure? The current schedule is not optimized");
                        if (userChoice == JOptionPane.YES_OPTION) {
                                PrintLog.writeToSchedule(PrintLog.dataToString(Schedule.getSchedule()));
                                JOptionPane.showMessageDialog(null, "Schedule is printed");
                                System.exit(0);
                        }

                });
                buttonPanel.add(volunteerButton);
                buttonPanel.add(rescheduleButton);
                buttonPanel.add(createSchedule);
                frame.add(titlePanel, BorderLayout.PAGE_START);
                frame.add(buttonPanel, BorderLayout.CENTER);
                return frame;
        }


        /**
         * This method creates a frame that prompts the user to print the schedule.
         * It utilizes a method from the PrintLog class to create a text file containing the schedule.
         * @return printScheduleFrame
         */
        public static JFrame createPrintScheduleFrame() {
                JFrame frame = new JFrame();
                frame.setSize(800, 500);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel titlePanel = new JPanel(new FlowLayout());
                JPanel buttonPanel = new JPanel(new FlowLayout());
                JLabel title = new JLabel("Your schedule is valid and ready to be printed");
                titlePanel.add(title);

                JButton button = new JButton("Print to Schedule");
                buttonPanel.add(button);
                button.addActionListener(e -> {
                        PrintLog.writeToSchedule(PrintLog.dataToString(Schedule.getSchedule()));
                        JOptionPane.showMessageDialog(null, "Schedule is printed");
                        System.exit(0);
                });
                frame.add(titlePanel, BorderLayout.PAGE_START);
                frame.add(buttonPanel, BorderLayout.CENTER);
                return frame;
        }

}
