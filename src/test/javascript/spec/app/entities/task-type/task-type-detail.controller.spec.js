'use strict';

describe('Controller Tests', function() {

    describe('TaskType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTaskType, MockTask, MockRequestType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTaskType = jasmine.createSpy('MockTaskType');
            MockTask = jasmine.createSpy('MockTask');
            MockRequestType = jasmine.createSpy('MockRequestType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TaskType': MockTaskType,
                'Task': MockTask,
                'RequestType': MockRequestType
            };
            createController = function() {
                $injector.get('$controller')("TaskTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ticketingSystemApp:taskTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
