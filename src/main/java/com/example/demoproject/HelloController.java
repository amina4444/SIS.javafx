package com.example.demoproject;

import com.example.demoproject.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class HelloController {

    @FXML
    private TextField emailInput;

    @FXML
    private TextField groupInput;

    @FXML
    private TextField idInput;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField surnameInput;
    @FXML
    private TextField gpaInput;

    @FXML
    private TextField searchField; // Для поиска студентов
    @FXML
    private Label infoLabel;

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> idColumn;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> surnameColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, String> groupColumn;
    @FXML private TableColumn<Student, Integer> gpaColumn;

    private ObservableList<Student> students = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Link columns to Student properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        gpaColumn.setCellValueFactory(new PropertyValueFactory<>("gpa"));


        idInput.setOnAction(e -> handleSaveChanges(null));
        nameInput.setOnAction(e -> handleSaveChanges(null));
        surnameInput.setOnAction(e -> handleSaveChanges(null));
        emailInput.setOnAction(e -> handleSaveChanges(null));
        groupInput.setOnAction(e -> handleSaveChanges(null));
        gpaInput.setOnAction(e -> handleSaveChanges(null));


        // Set data list to the table
        studentTable.setItems(students);
        //this.readCSV();
        this.readJSON();
        setupContextMenu();

        // Фильтрация по поиску
        FilteredList<Student> filteredStudents = new FilteredList<>(students, s -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredStudents.setPredicate(student -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return String.valueOf(student.getId()).contains(lower) ||
                        student.getName().toLowerCase().contains(lower) ||
                        student.getSurname().toLowerCase().contains(lower);
            });
        });
        studentTable.setItems(filteredStudents);

    }
    private void setupContextMenu() {
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteItem = new MenuItem("Delete");
        editItem.setOnAction(e -> {
            Student selected = studentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                idInput.setText(String.valueOf(selected.getId()));
                nameInput.setText(selected.getName());
                surnameInput.setText(selected.getSurname());
                emailInput.setText(selected.getEmail());
                groupInput.setText(selected.getGroup());
                gpaInput.setText(String.valueOf(selected.getGpa()));
            }
        });

        deleteItem.setOnAction(e -> {
            Student selected = studentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                students.remove(selected);
                studentTable.refresh();
            }
        });


        ContextMenu contextMenu = new ContextMenu(editItem, deleteItem);
        studentTable.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>();

            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    studentTable.getSelectionModel().select(row.getItem()); // выделяем строку
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            row.setOnMouseClicked(event -> {
                if (!event.isSecondaryButtonDown()) {
                    contextMenu.hide();
                }
            });

            return row;
        });
    }



    @FXML
    void handleSave(ActionEvent event) {
//        try {
//            int id = Integer.parseInt(idInput.getText());
//            String name = nameInput.getText();
//            String surname = surnameInput.getText();
//            String email = emailInput.getText();
//            String group = groupInput.getText();
//            int gpa =  Integer.parseInt(gpaInput.getText());
//
//            Student student = new Student(id, name, surname);
//            student.setEmail(email);
//            student.setGroup(group);
//            student.setGpa(gpa);
//
//            students.add(student);// when we add students list, tableview is updated automatically
//            studentTable.refresh();
//
//            System.out.println("Student successfully added to the list.");
//            idInput.setText("");
//            nameInput.setText("");
//            surnameInput.setText("");
//            emailInput.setText("");
//            groupInput.setText("");
//            gpaInput.setText("");
//
//        }
//        catch(Exception e){
//            System.out.println(e.getMessage());
//        }


        try {
            if(idInput.getText().isEmpty() || nameInput.getText().isEmpty() || surnameInput.getText().isEmpty()) {
                infoLabel.setText("ID, Name and Surname are required!");
                return;
            }
            int id = Integer.parseInt(idInput.getText());
            int gpa = Integer.parseInt(gpaInput.getText());
            if(gpa < 0 || gpa > 100) { infoLabel.setText("GPA must be 0-100"); return; }

            Student student = new Student(id, nameInput.getText(), surnameInput.getText(),
                    emailInput.getText(), groupInput.getText(), gpa);
            students.add(student);
            studentTable.refresh();

            // Очистка полей
            idInput.clear(); nameInput.clear(); surnameInput.clear();
            emailInput.clear(); groupInput.clear(); gpaInput.clear();
            infoLabel.setText("Student added!");
        } catch(Exception e) {
            infoLabel.setText("Invalid input");
        }


    }
    @FXML
    void handleSaveChanges(ActionEvent event) {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setId(Integer.parseInt(idInput.getText()));
                selected.setName(nameInput.getText());
                selected.setSurname(surnameInput.getText());
                selected.setEmail(emailInput.getText());
                selected.setGroup(groupInput.getText());
                selected.setGpa(Integer.parseInt(gpaInput.getText()));

                studentTable.refresh(); // Обновляем таблицу

                System.out.println("Changes saved.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("No student selected.");
        }
    }


    @FXML
    void onPrintClick(){

        for(Student s: students){
            System.out.println(s);
        }

    }
    private void readCSV(){
        try{
            String filename = "src/main/resources/com/example/demoproject/filename.txt";
            Scanner input = new Scanner(new File(filename));
            if (input.hasNextLine()) {
                input.nextLine(); // Skip the first line (header)
            }

            while (input.hasNextLine()) {
                String line = input.nextLine();
                // Split by comma (CSV format)
                String[] data = line.split(",");

                // Extract student information
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                String surname = data[2];
                String email = data[3];
                String group = data[4];
                int gpa = Integer.parseInt(data[5]);

                Student student = new Student(id, name, surname, email, group,  gpa);
                students.add(student);

            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void saveCSV(){
        try {
            String filename = "src/main/resources/com/example/demoproject/filename.txt";

            FileWriter myWriter = new FileWriter(filename);
            myWriter.write("id,name,surname,email,group\n,gpa");

            for(Student s: this.students){
                myWriter.write(s + "\n");
            }
            myWriter.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    private void saveJSON(){
        ObjectMapper objectMapper = new ObjectMapper();
        String filename = "src/main/resources/com/example/demoproject/data.json";

        try {
            // Convert Java object to JSON and write to a file
            objectMapper.writeValue(new File(filename), students);
            System.out.println("JSON file created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readJSON(){
        ObjectMapper objectMapper = new ObjectMapper();
        String filename = "src/main/resources/com/example/demoproject/data.json";
        try {
            // Read JSON file into Student array
            Student[] studentsArray = objectMapper.readValue(new File(filename), Student[].class);

            // Convert array to List
            Collections.addAll(students, studentsArray);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onSave(ActionEvent event) {

        this.saveCSV();
        this.saveJSON();
    }
}
