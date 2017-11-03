(function() {
    'use strict';

    angular
        .module('ticketingSystemApp')
        .controller('TaskTypeDialogController', TaskTypeDialogController);

    TaskTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TaskType', 'Task', 'RequestType'];

    function TaskTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TaskType, Task, RequestType) {
        var vm = this;

        vm.taskType = entity;
        vm.clear = clear;
        vm.save = save;
        vm.tasks = Task.query();
        vm.requesttypes = RequestType.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.taskType.id !== null) {
                TaskType.update(vm.taskType, onSaveSuccess, onSaveError);
            } else {
                TaskType.save(vm.taskType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ticketingSystemApp:taskTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
