describe('Using the wizardService, I can ', function() {

    var wizardService;

    var handle1 = 'step1';
    var handle2 = 'step2';
    var handle3 = 'step3';

    function getStepId(stepHandle) {
        return stepHandle + '.id';
    }

    function getStepName(stepHandle) {
        return stepHandle + '.name';
    }

    function getStepTitle(stepHandle) {
        return stepHandle + '.title';
    }

    function getStepUrl(stepHandle) {
        return stepHandle + '.url';
    }

    function createDummyStep(stepHandle) {
        return {
            id: getStepId(stepHandle),
            name: getStepName(stepHandle),
            title: getStepTitle(stepHandle),
            templateUrl: getStepUrl(stepHandle)
        };
    }

    function initDefaultData() {
        wizardService.initialize({
            steps: [
                createDummyStep(handle1),
                createDummyStep(handle2),
                createDummyStep(handle3)
            ]
        });
    }

    beforeEach(module('wizardServiceModule'));

    beforeEach(inject(function(wizardServiceFactory) {
        wizardService = wizardServiceFactory.newWizardService();
    }));


    it('add the proper number of steps to the wizard', function() {
        var config1 = {
            steps: [
                createDummyStep(handle1)
            ]
        };
        var config2 = {
            steps: [
                createDummyStep(handle1),
                createDummyStep(handle2)
            ]
        };
        var config3 = {
            steps: [
                createDummyStep(handle1),
                createDummyStep(handle2),
                createDummyStep(handle3)
            ]
        };
        wizardService.initialize(config1);
        expect(wizardService.getStepsCount()).toBe(1);
        wizardService.initialize(config2);
        expect(wizardService.getStepsCount()).toBe(2);
        wizardService.initialize(config3);
        expect(wizardService.getStepsCount()).toBe(3);
    });


    it('begin with the first step', function() {
        initDefaultData();
        var currentStep = wizardService.getCurrentStep();
        var stepHandle = 'step1';

        expect(currentStep.id).toEqual(getStepId(stepHandle));
        expect(currentStep.name).toEqual(getStepName(stepHandle));
        expect(currentStep.title).toEqual(getStepTitle(stepHandle));
        expect(currentStep.templateUrl).toEqual(getStepUrl(stepHandle));
    });


    xit('load a step by id', function() {
        initDefaultData();
        wizardService.goToStepWithId(getStepId(handle2));
        var currentStep = wizardService.getCurrentStep();

        expect(currentStep.id).toEqual(getStepId(handle2));
        expect(currentStep.name).toEqual(getStepName(handle2));
        expect(currentStep.title).toEqual(getStepTitle(handle2));
        expect(currentStep.templateUrl).toEqual(getStepUrl(handle2));
    });

    it('load a step by index', function() {
        initDefaultData();
        expect(wizardService.getCurrentStepIndex()).toEqual(0);

        wizardService.goToStepWithIndex(2);
        expect(wizardService.getCurrentStepIndex()).toEqual(2);

        wizardService.goToStepWithIndex(0);
        expect(wizardService.getCurrentStepIndex()).toEqual(0);
    });

    xit('get the index of the current step', function() {
        initDefaultData();
        expect(wizardService.getCurrentStepIndex()).toEqual(0);

        wizardService.goToStepWithId(getStepId(handle3));
        expect(wizardService.getCurrentStepIndex()).toEqual(2);
    });

    it('see an exception if I pass duplicate step ids', function() {

        var init = wizardService.initialize.bind(wizardService, {
            steps: [
                createDummyStep(handle1),
                createDummyStep(handle1),
            ]
        });

        expect(init).toThrow('Invalid (Duplicate) step id: step1.id');
    });

    it('provide no stepUid and have one generated for me', function() {
        var step1 = createDummyStep(handle1);
        step1.id = null;
        wizardService.initialize({
            steps: [step1]
        });

        expect(wizardService.getCurrentStep()).not.toBe(null);
    });

    xit('remove a step by id', function() {
        initDefaultData();

        expect(wizardService.getStepsCount()).toBe(3);

        wizardService.removeStepById(getStepId(handle3));
        expect(wizardService.getStepsCount()).toBe(2);
        expect(wizardService.containsStep(getStepId(handle3))).toBe(false);
    });

    xit('remove a step by index', function() {
        initDefaultData();

        expect(wizardService.getStepsCount()).toBe(3);

        wizardService.removeStepByIndex(1);
        expect(wizardService.getStepsCount()).toBe(2);
        expect(wizardService.containsStep(getStepId(handle2))).toBe(false);
    });

    it('add a step before the current step', function() {
        wizardService.initialize({
            steps: [createDummyStep(handle1), createDummyStep(handle2)]
        });

        expect(wizardService.getStepsCount()).toBe(2);

        wizardService.addStep(createDummyStep(handle3), 0);
        expect(wizardService.getStepsCount()).toBe(3);
        expect(wizardService.getCurrentStepIndex()).toBe(1);
    });

    it('add a step after the current step', function() {
        wizardService.initialize({
            steps: [createDummyStep(handle1), createDummyStep(handle2)]
        });

        expect(wizardService.getStepsCount()).toBe(2);

        wizardService.addStep(createDummyStep(handle3), 1);
        expect(wizardService.getStepsCount()).toBe(3);
        expect(wizardService.getCurrentStepIndex()).toBe(0);
    });





});
