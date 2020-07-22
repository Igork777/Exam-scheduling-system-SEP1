/*globals $:false*/
//VARIABLE DECLARATIONS
const text = "<exams> <exam><course>SDJ1</course><type>Oral</type><teacher>Allen Henrik</teacher><classroom>E.109</classroom><class>Exam</class><date>1/1/2021</date><time>08:00</time><duration>180</duration></exam><exam><course>RWD</course><type>Written</type><teacher>Line Egsgaard</teacher><classroom>E.308</classroom><class>Aj</class><date>1/1/2021</date><time>10:35</time><duration>30</duration></exam></exams>";
const parser = new DOMParser();
let xmlDoc = parser.parseFromString(text, "text/xml");
const classSelection = $("#classSelect");
const teacherSelection = $("#teacherSelect");
const courseSelection = $("#courseSelect");
const content = $("#table_content");


//RUNNING CODE
addInformationToSelections();
$(content).html(createContent());


//FUNCTION DECLARATIONS
function createContent() {
    let exams = xmlDoc.getElementsByTagName("exam");
    let returnText = "";
    for (let i = 0; i < exams.length; i++) {
        returnText += "<tr><td>" + exams[i].getElementsByTagName("class")[0].childNodes[0].nodeValue +"</td>" +
            "<td>" + exams[i].getElementsByTagName("course")[0].childNodes[0].nodeValue + "</td>" +
            "<td>" + exams[i].getElementsByTagName("type")[0].childNodes[0].nodeValue + "</td>" +
            "<td>" + exams[i].getElementsByTagName("teacher")[0].childNodes[0].nodeValue + "</td>" +
            "<td>" + exams[i].getElementsByTagName("date")[0].childNodes[0].nodeValue + "</td>" +
            "<td>" + examDuration(exams[i].getElementsByTagName("date")[0].childNodes[0].nodeValue, exams[i].getElementsByTagName("time")[0].childNodes[0].nodeValue, exams[i].getElementsByTagName("duration")[0].childNodes[0].nodeValue) + "</td>" +
            "<td>" + exams[i].getElementsByTagName("classroom")[0].childNodes[0].nodeValue + "</td>" +
            "</tr>"
    }
    return returnText;
}
function makeTableFrom(filtered){}

function examDuration(date, time, duration) {
    let dateParts = date.split("/");
    let day = dateParts[0];
    let month = dateParts[1];
    let year = dateParts[2];
    let timeParts = time.split(":");
    let hour = timeParts[0];
    let minute = timeParts[1];
    let start = new Date(year, month, day, hour, minute);
    duration = parseInt(duration);
    let end = new Date(start.getTime() + duration*60000);

    let returnString="";
    returnString += start.toLocaleTimeString();
    returnString += "-";
    returnString += end.toLocaleTimeString();
    return returnString;
}

function addClassesToClassSelection() {
    let allClasses = Array.prototype.slice.call(xmlDoc.getElementsByTagName("class"));
    let classesNames = allClasses.map(function(item) {
        return item.childNodes[0].nodeValue;
    });
    let filteredClasses = classesNames.filter((item, index) => classesNames.indexOf(item) >= index);
    $(classSelection).append(`<option value="all">All</option>`);
    for (let i = 0; i < filteredClasses.length; i++) {
        $(classSelection).append(`<option value="${filteredClasses[i]}"> 
                                       ${filteredClasses[i]} 
                                  </option>`);
    }
}

function addTeachersToTeacherSelection() {
    let allTeachers = Array.prototype.slice.call(xmlDoc.getElementsByTagName("teacher"));
    let teachersNames = allTeachers.map(function(item) {
        return item.childNodes[0].nodeValue;
    });
    let filteredClasses = teachersNames.filter((item, index) => teachersNames.indexOf(item) >= index);
    $(teacherSelection).append(`<option value="all">All</option>`);
    for (let i = 0; i < filteredClasses.length; i++) {
        $(teacherSelection).append(`<option value="${filteredClasses[i]}"> 
                                       ${filteredClasses[i]} 
                                  </option>`);
    }
}

function addCoursesToCourseSelection() {
    let allCourses = Array.prototype.slice.call(xmlDoc.getElementsByTagName("course"));
    let coursesNames = allCourses.map(function(item) {
        return item.childNodes[0].nodeValue;
    });
    let filteredCourses = coursesNames.filter((item, index) => coursesNames.indexOf(item) >= index);
    $(courseSelection).append(`<option value="all">All</option>`);
    for (let i = 0; i < filteredCourses.length; i++) {
        $(courseSelection).append(`<option value="${filteredCourses[i]}"> 
                                       ${filteredCourses[i]} 
                                  </option>`);
    }
}

function addInformationToSelections() {
    addClassesToClassSelection();
    addTeachersToTeacherSelection();
    addCoursesToCourseSelection();
}