package assignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class EmployeeRoster {
	
	private static LinkedList<Employee> roster =  new LinkedList<Employee>();
    private int badRecords = 0;
    private int InvalidEmployeeType;

	
	/*Read from text file <<fileName>>, load to an LinkedList of Employees, handle exceptions in this method.
	For each line in the text file:
	
	Tokens in text file is separated by #
	SM#Daffy#Duck#S913101#3.9#3000##  - SouthernStudentsMth example
    SH#Mickey#Mouse#S924111#2.9#12#35 - SouthernStudentHrly example
	1st Token:Employees are either SM - SouthernStudentsMth, SH - SouthernStudentHrly, otherwise throw an exception, include the message "Invalid Employee Type"
	2nd Token: First Name
	3rd Token: Last Name
	4th Token: Student ID
	5th Token: Student GPA
	6th Token: If this is a SouthernStudentsMth, then this token is for monthly pay amount. 
	           If this is a SouthernStudentsHrly, then this token is for hourly rate.
	7th Token: If this is a SouthernStudentsHrly, then this token is for hourly total hours worked.	
	           If this is a SouthernStudentsHrly, then this token is for not used.
	If number of tokens is not exactly 7, throw an exception, include the message "Bad Record"
	
	Catch the exceptions, update the class field for each of the two exception messages outlined above
	Any IOExceptions should be handled by displaying the following message to the console "We had a problem locating the file" 
	*/
	public void loadEmployeesFromFile(String filename) {
		try {
            List<String> allEmployees = Files.readAllLines(Path.of(filename));
            for(int i = 0; i < allEmployees.size(); i++) {
                    try {
                            String[] oneEmployee = allEmployees.get(i).split("#");
                            if(oneEmployee.length < 7) throw new Exception("Bad record");
                            else {
                                    if(oneEmployee[0].equalsIgnoreCase("SM")) {
                                            SouthernStudentMth ssm = new SouthernStudentMth(oneEmployee[1], oneEmployee[2]);
                                            ssm.addId(oneEmployee[3]);
                                            ssm.setGpa(Double.parseDouble(oneEmployee[4]));
                                            ssm.setMonthlyPay(Double.parseDouble(oneEmployee[5]));
                                            roster.add(ssm);
                                    }
                                    else if(oneEmployee[0].equalsIgnoreCase("SH")) {
                                            SouthernStudentHrly ssh = new SouthernStudentHrly(oneEmployee[1], oneEmployee[2]);
                                            ssh.addId(oneEmployee[3]);
                                            ssh.setGpa(Double.parseDouble(oneEmployee[4]));
                                            ssh.setHourlyRate(Double.parseDouble(oneEmployee[5]));
                                            ssh.setTotalHoursForWeek(Integer.parseInt(oneEmployee[6].trim()));
                                            roster.add(ssh);
                                    }
                                    else if(oneEmployee[0].equalsIgnoreCase("EH")) {
                                            EasternStudentHrly esh = new EasternStudentHrly(oneEmployee[1], oneEmployee[2]);
                                            esh.addId(oneEmployee[3]);
                                            esh.setGpa(Double.parseDouble(oneEmployee[4]));
                                            esh.setHourlyRate(Double.parseDouble(oneEmployee[5]));
                                            esh.setTotalHoursForWeek(Integer.parseInt(oneEmployee[6].trim()));
                                            roster.add(esh);
                                    }else throw new Exception("Invalid Employee Type");
                            }
                    }catch (Exception e) {
                            if(e.getMessage().equalsIgnoreCase("Bad record")) this.badRecords++;
                            if(e.getMessage().equalsIgnoreCase("Invalid Employee Type")) this.InvalidEmployeeType++;
                    }
            }
    } catch (IOException e) {
            System.out.println("We had a problem locating the file");
            System.exit(1);
    }
		
	}
	
	//Setter
	public void setRoster(LinkedList<Employee> roster) {
		this.roster=roster;
	}

	//sum the pays of all employees
	public double computePayrollForAllEmployees() {
		double total = 0.0;
        for(int i=0; i < roster.size(); i++) {
                total += roster.get(i).getPay(); 
        }
        return total;
	}
	/*
	 Writes to a text file <<filename>> handle exceptions in this method
	 This method uses the class field roster list, finds only the SouthernStudentsMth object and writes their info to a text file
	 Each line will have the toString info for one SouthernStudentsMonthly object
	 This method returns a count of how many employee objects were written to the file
	 
	 If the filename and path used belongs to an existing file, do not overwrite it. Instead display the following message to the console "Cannot overwrite an existing file!", then end the program. 
	 All other IOExceptions should be handled by displaying the following message to the console "We had a problem locating the file" 
	 */
	public int saveSouthernStudentsMonthlyOnly(String filename) {
		int result = 0;
        if(Files.exists(Path.of(filename))) {
                System.out.println("Cannot overwrite an existing file!");
                return -1;
        }else {
                try {
                        Files.createFile(Path.of(filename));
                        for(int i = 0; i <roster.size(); i++) {
                                if(roster.get(i) instanceof SouthernStudentMth) {
                                                Files.write(Path.of(filename),roster.get(i).toString().getBytes(),StandardOpenOption.APPEND);
                                                result++;
                                }
                        }
                } catch (IOException e) {
                        System.out.println("We had a problem locating the file");
                        System.exit(1);
                }
        }
        return result;
	}

	//Getter
	public int getBadRecords() {
		return badRecords;
	}
	//Getter
	public int getInvalidEmployeeType() {
		return InvalidEmployeeType;
	}
	//Getter
	public LinkedList<Employee> getRoster() {
		return roster;
	}
}
