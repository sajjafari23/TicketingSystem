(function() {
    'use strict';

    angular
        .module('ticketingSystemApp')
        .controller('RequestTypeDeleteController',RequestTypeDeleteController);

    RequestTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'RequestType'];

    function RequestTypeDeleteController($uibModalInstance, entity, RequestType) {
        var vm = this;

        vm.requestType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RequestType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
