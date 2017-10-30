(function() {
    angular.module('addPageInfoDirectiveModule', ['genericEditorModule'])
        .directive('pageInfoForm', function(GenericEditor) {
            return {
                templateUrl: 'web/common/services/genericEditor/genericEditorTemplate.html',
                restrict: 'E',
                transclude: true,
                scope: {
                    page: '=',
                    structure: '=',
                    sharedData: '=',
                    onSubmit: '&'
                },
                link: function($scope) {
                    // Initialize the structure required by the generic editor.
                    $scope.editor = new GenericEditor({
                        structureApi: null,
                        smarteditComponentType: "",
                        structure: $scope.structure,
                        onSubmit: $scope.onSubmit,
                        content: $scope.page
                    });

                    $scope.sharedData.editor = $scope.editor;
                    $scope.sharedData.componentForm = $scope.componentForm;

                    $scope.editor.init();
                }
            };
        });
})();
