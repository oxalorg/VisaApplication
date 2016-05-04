$( document ).ready(function() {
    var baseURL = "http://127.0.0.1:8181"
    function setEmployeeDetails(empCode){
        var request = $.getJSON(baseURL + "/api/get/employeeDetails?empCode=" + empCode, function(data){
            $("#displayCode").html(data['emp_code']);
            $("#displayName").html(data['emp_name']);
            $("#displayContact").html(data['phone_no']);
            $("#displayProjCode").html(data['proj_code']);
            $("#displayDept").html(data['department']);
        });
        request.fail(function(){
            alert("Something went wrong while fetching profile details!");
        });
    }
});
