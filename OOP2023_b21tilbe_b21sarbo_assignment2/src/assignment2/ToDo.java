package assignment2;

//import swing libraries

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;

import se.his.it401g.todo.HomeTask;
import se.his.it401g.todo.StudyTask;
import se.his.it401g.todo.Task;
import se.his.it401g.todo.TaskListener;

public class ToDo implements TaskListener, ActionListener { //implements two interfaces
    JButton button1, button2, button3;
    JPanel taskPanel;
    JScrollPane scrollPane;
    private ArrayList<Task> allTasks = new ArrayList<Task>(); //array with tasks

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDo().createAndShowGUI());
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setBounds(100, 100, 800, 600); //frame size

        JButton button1 = new JButton("New HomeTask"); //add HomeTask buttons
        JButton button2 = new JButton("New StudyTask"); //add StudyTask buttons
        JButton button3 = new JButton("New MyCustomTask"); //add MyCustomTask buttons

        JPanel buttonPanel = new JPanel(); //create buttonPanel
        buttonPanel.add(button1); //add the button to the panel
        buttonPanel.add(button2); //add the button to the panel
        buttonPanel.add(button3); //add the button to the panel

        String[] options = {"Alphabetically", "Checked", "Unchecked"}; //String with sorting alternatives
        JComboBox<String> sortOptions = new JComboBox<String>(options);
        buttonPanel.add(sortOptions); //add sorting options in buttonPanel
        
        sortOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (sortOptions.getSelectedItem().equals("Alphabetically")) { //sort the taskPanel alphabetically
            	    allTasks.sort((t1, t2) -> t1.getText().compareToIgnoreCase(t2.getText()));
            	    taskPanel.removeAll();
            	    for (Task task : allTasks) {
            	        taskPanel.add(task.getGuiComponent());
            	    }
            	    taskPanel.revalidate();
            	    taskPanel.repaint();
            	}
            	else if (sortOptions.getSelectedItem().equals("Checked")) { //sort the taskPanel with completed tasks
            	    ArrayList<Task> completedTasks = allTasks.stream()
                         .filter(Task::isComplete)
                         .collect(Collectors.toCollection(ArrayList::new));
            	    completedTasks.sort((t1, t2) -> t1.getText().compareToIgnoreCase(t2.getText()));
            	    taskPanel.removeAll();
            	    for (Task task : completedTasks) {
            	        taskPanel.add(task.getGuiComponent());
            	    }
            	    taskPanel.revalidate();
            	    taskPanel.repaint();
            	}
            	else if (sortOptions.getSelectedItem().equals("Unchecked")) { //sort the taskPanel with uncompleted tasks
            	    ArrayList<Task> taskUncompleted = allTasks.stream()
                       .filter(task -> !task.isComplete())
                       .collect(Collectors.toCollection(ArrayList::new));
            	    taskUncompleted.sort((t1, t2) -> t1.getText().compareToIgnoreCase(t2.getText()));
            	    taskPanel.removeAll();
            	    for (Task task : taskUncompleted) {
            	        taskPanel.add(task.getGuiComponent());
            	    }
            	    taskPanel.revalidate();
            	    taskPanel.repaint();
            	}
            }
        });

        button1.addActionListener(this); //add ActionListener
        button2.addActionListener(this); //add ActionListener
        button3.addActionListener(this); //add ActionListener

        taskPanel = new JPanel(); //create taskPanel

        BoxLayout boxlayout = new BoxLayout(taskPanel, BoxLayout.Y_AXIS); //layout

        taskPanel.setLayout(boxlayout); //layout

        scrollPane = new JScrollPane(taskPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); //adds a scrollBar if necessary
        scrollPane.setPreferredSize(new Dimension(800, 500));

        frame.add(buttonPanel, BorderLayout.NORTH); //layout
        frame.add(scrollPane, BorderLayout.CENTER); //layout

        frame.setVisible(true);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) { //create different tasks
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button.getText().equals("New HomeTask")) { //create new HomeTask
                Task task = new HomeTask();
                task.setTaskListener(this);
                allTasks.add(task);
                taskPanel.add(task.getGuiComponent());
            } else if (button.getText().equals("New StudyTask")) { //create new StudyTask
                Task task = new StudyTask();
                task.setTaskListener(this);
                allTasks.add(task);
                taskPanel.add(task.getGuiComponent());
            } else if (button.getText().equals("New MyCustomTask")) { //create new MyCustomTask
                Task task = new MyCustomTask();
                task.setTaskListener(this);
                allTasks.add(task);
                taskPanel.add(task.getGuiComponent());
            }

            taskPanel.revalidate();
            taskPanel.repaint();
        }
    }
    
    public void changeTaskName(Task t, String newName) { //Makes the user able to change the name
        if (t instanceof HomeTask) {
            ((HomeTask) t).setName(newName);
        } else if (t instanceof StudyTask) {
            ((StudyTask) t).setName(newName);
        } else if(t instanceof MyCustomTask) {
            ((StudyTask) t).setName(newName);
        }
    }

    @Override
    public void taskChanged(Task t) { //Makes the user able to change the name
        String newName = "New name";
        changeTaskName(t, newName);
    }


    @Override
    public void taskCompleted(Task t) { //task completed
        int completedTasks = 0;
        for (Task task : allTasks) {
            if (task.isComplete()) {
                completedTasks++;
            }
        }
        System.out.println("Number of tasks completed: " + completedTasks); //writes out the number of tasks completed
    }

    @Override
    public void taskUncompleted(Task t) {
        int completedTasks = 0;
        for (Task task : allTasks) {
            if (task.isComplete()) {
                completedTasks++;
            }
        }
        System.out.println("Number of tasks completed: " + completedTasks); //writes out the number of tasks completed
    }


    @Override
    public void taskCreated(Task t) {
    	
    }

    @Override
    public void taskRemoved(Task t) { //makes the user able to remove a task
        taskPanel.remove(t.getGuiComponent());
        allTasks.remove(t);
        taskPanel.revalidate();
        taskPanel.repaint();
    }

}


