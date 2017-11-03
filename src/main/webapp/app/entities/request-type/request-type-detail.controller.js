(function() {
    'use strict';

    angular
        .module('ticketingSystemApp')
        .controller('RequestTypeDetailController', RequestTypeDetailController);

    RequestTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RequestType', 'TaskType'];

    function RequestTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, RequestType, TaskType) {
        var vm = this;

        vm.requestType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ticketingSystemApp:requestTypeUpdate', function(event, result) {
            vm.requestType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
