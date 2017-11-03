(function() {
    'use strict';

    angular
        .module('ticketingSystemApp')
        .controller('TaskTypeDetailController', TaskTypeDetailController);

    TaskTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TaskType', 'Task', 'RequestType'];

    function TaskTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, TaskType, Task, RequestType) {
        var vm = this;

        vm.taskType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ticketingSystemApp:taskTypeUpdate', function(event, result) {
            vm.taskType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
