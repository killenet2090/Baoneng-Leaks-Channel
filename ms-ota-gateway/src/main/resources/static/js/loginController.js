var app = angular.module('loginApp', ['Encrypt']);
app.controller('loginCtrl', function($scope,$http,Md5,Base64,Sha1) {
	$scope.login = function() {
		$http({
			method: 'GET',
			url: 'http://localhost:8013/user/queryUserByCondiction',
			params: {
			  'name': $scope.name,
			  'password': $scope.password
			}
		}).then(function successCallback(response) {
		        var code = response.data.code;
		        var start = code.indexOf("2");
		        if(start == -1){
		        	alert(response.data.message);
		        }
		        //var date = new Date();
		        //var content = $scope.password;
		        //var md5Value = Md5.hex_md5(content);
		        window.location.href = "http://localhost:8013/index.html";
			}, 
			function errorCallback(response) {
			    //var code = response.data.code;
		        //var start = code.indexOf("2");
		        //if(start == -1){
		        	//alert(response.data.message);
		        //}
		        //window.location.href = "http://localhost:8013/error.html";
		});
	};
});