# Assignment 4: Implementing Control Structures

## Files
- `employee_scheduler.py` - Python implementation
- `EmployeeScheduler.java` - Java implementation
- `sample_output.txt` - sample Python run
- `java_sample_output.txt` - sample Java run
- `Assignment4_Report.docx` - submission report with explanation, references, and output screenshot

## Run Python
`python3 employee_scheduler.py`

## Run Java
`javac EmployeeScheduler.java`
`java EmployeeScheduler`

Press Enter at the prompt to use reproducible sample preferences. Enter `M` to manually enter each employee's daily preference.

## Design checks
Both implementations validate minimum staffing, the five-day weekly maximum, and the one-shift-per-day restriction. The shortage pass reassigns eligible employees to an open shift on the same day while respecting those limits.
