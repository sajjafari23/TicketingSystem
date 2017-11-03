(function() {
    'use strict';

    angular
        .module('ticketingSystemApp')
        .controller('RequestDialogController', RequestDialogController);

    RequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Request', 'Priority', 'RequestType'];

    function RequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Request, Priority, RequestType) {
        var vm = this;

        vm.request = entity;
        vm.clear = clear;
        vm.save = save;
        vm.priorities = Priority.query();
        vm.requesttypes = RequestType.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.request.id !== null) {
                Request.update(vm.request, onSaveSuccess, onSaveError);
            } else {
                Request.save(vm.request, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ticketingSystemApp:requestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
