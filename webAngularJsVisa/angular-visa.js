angular.module('app', [])
.controller('EmployeeController', ['$scope', '$http', function ($scope, $http){
        this.showEmployee = false;
        setTimeout(function(){
            this.showEmployee = true;
        });

        console.dir(this.employee);
        this.getEmployeeDetails = function getEmployeeDetails($scope){
            $http.get('http://127.0.0.1:8181/api/get/employeeDetails?empCode='+this.empCode)
            .then(function(response) {
                this.employee = response.data;
                $scope.$apply();
                console.dir(this.employee);
            }, function(response){
                console.log(response);
            });
        };
}]);
