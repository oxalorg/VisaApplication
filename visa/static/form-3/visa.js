$( document ).ready(function() {
    console.log("LOL");
    $("#logoutBtn").hide(0);
    $("#employeeHome").hide(0);

    var empCode = null;
    var baseURL = "http://server1.miteshshah.com"
    $("#login").submit(function(event){
        empCode = $("#empCode").val();
        var $form = $(this);
        var $inputs = $form.find("input, select, textarea");
        var serializedData = $form.serialize();
        console.log(serializedData);
        request = $.ajax({
            url: baseURL + "/api/login",
            type: "post",
            data: serializedData
        });
        request.done(function (response, textStatus, jqXHR){
        // Log a message to the console
            alert("Logged in!");
            responseJSON = $.parseJSON(response);
            if (responseJSON["success"] == 1) {
                $("#login").hide('slow');
                $("#logoutBtn").show('slow');
                $("#employeeHome").show('slow');
                $("#top-content").hide(100);
                setEmployeeDetails(empCode);
            } else {
                alert("Bad Credentials. Please try again!");
            }
        });
        request.fail(function(response){
            console.dir(response);
        });
        event.preventDefault();
    });

    $("#logoutBtn").click(function(event){
        $("#employeeHome").hide('slow');
        $("#logoutBtn").hide('slow');
        $("#top-content").show('slow');
        $("#displayCode").html("");
        $("#displayName").html("");
        $("#displayContact").html("");
        $("#displayProjCode").html("");
        $("#displayDept").html("");
        event.preventDefault();
    })

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
