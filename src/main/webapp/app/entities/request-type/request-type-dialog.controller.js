(function() {
    'use strict';

    angular
        .module('ticketingSystemApp')
        .controller('RequestTypeDialogController', RequestTypeDialogController);

    RequestTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RequestType'];

    function RequestTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RequestType) {
        var vm = this;

        vm.requestType = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.requestType.id !== null) {
                RequestType.update(vm.requestType, onSaveSuccess, onSaveError);
            } else {
                RequestType.save(vm.requestType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ticketingSystemApp:requestTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
