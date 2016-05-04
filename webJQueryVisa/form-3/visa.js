$( document ).ready(function() {
    $("#logoutBtn").hide(0);
    $("#employeeHome").hide(0);

    var empCode = null;
    var baseURL = "http://127.0.0.1:8181"
    $("#login").submit(function(event){
        empCode = $("#empCode").val();
        var $form = $(this);
        var $inputs = $form.find("input, select, textarea");
        var serializedData = $form.serialize();
        console.log(serializedData);
        request = $.ajax({
            url: baseURL + "/api/login/",
            type: "post",
            data: serializedData
        });
        request.done(function (response, textStatus, jqXHR){
        // Log a message to the console
            responseJSON = $.parseJSON(response);
            if (responseJSON["success"] == 1) {
                $("#login").hide('slow');
                $("#logoutBtn").show('slow');
                $("#employeeHome").show('slow');
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
        $("#login").show('slow');
        $("#displayCode").html("");
        $("#displayName").html("");
        $("#displayContact").html("");
        $("#displayProjCode").html("");
        $("#displayDept").html("");
        event.preventDefault();
    })
});
