(function() {
    'use strict';

    angular
        .module('ticketingSystemApp')
        .controller('TaskTypeDeleteController',TaskTypeDeleteController);

    TaskTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'TaskType'];

    function TaskTypeDeleteController($uibModalInstance, entity, TaskType) {
        var vm = this;

        vm.taskType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TaskType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
