package teammates.common.datatransfer.questions;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import teammates.test.BaseTestCase;

/**
 * SUT: {@link FeedbackNumericalScaleQuestionDetails}.
 */
public class FeedbackNumericalScaleQuestionDetailsTest extends BaseTestCase {

    @Test
    public void testIsInstructorCommentsOnResponsesAllowed_shouldReturnTrue() {
        FeedbackQuestionDetails feedbackQuestionDetails = new FeedbackNumericalScaleQuestionDetails();
        assertTrue(feedbackQuestionDetails.isInstructorCommentsOnResponsesAllowed());
    }

    @Test
    public void testIsFeedbackParticipantCommentsOnResponsesAllowed_shouldReturnFalse() {
        FeedbackQuestionDetails feedbackQuestionDetails = new FeedbackNumericalScaleQuestionDetails();
        assertFalse(feedbackQuestionDetails.isFeedbackParticipantCommentsOnResponsesAllowed());
    }

    @Test
    public void tesValidateResponseDetails() {
        FeedbackNumericalScaleQuestionDetails numScaleQuestion = new FeedbackNumericalScaleQuestionDetails();
        numScaleQuestion.setStep(0.1);

        ______TS("Test Val=1.2 and Step=0.1 does no trigger error");
        FeedbackNumericalScaleResponseDetails resp = new FeedbackNumericalScaleResponseDetails();
        resp.setAnswer(1.2);
        List<String> errors = numScaleQuestion.validateResponsesDetails(Arrays.asList(resp), 1);
        assertTrue(errors.isEmpty());

        ______TS("Test Val=1.22 and Step=0.1 triggers error");
        resp = new FeedbackNumericalScaleResponseDetails();
        resp.setAnswer(1.22);
        errors = numScaleQuestion.validateResponsesDetails(Arrays.asList(resp), 1);
        assertEquals(1, errors.size());
        assertEquals("Please enter a valid value. The two nearest valid values are 1.2 and 1.3.", errors.get(0));

        ______TS("Test Val=1.333 and Step=0.1 triggers error");
        resp = new FeedbackNumericalScaleResponseDetails();
        resp.setAnswer(1.333);
        errors = numScaleQuestion.validateResponsesDetails(Arrays.asList(resp), 1);
        assertEquals(1, errors.size());
        assertEquals("Please enter a valid value. The two nearest valid values are 1.3 and 1.4.", errors.get(0));

        ______TS("Test Val=2 and Step=0.1 does not trigger error");
        resp = new FeedbackNumericalScaleResponseDetails();
        resp.setAnswer(2);
        errors = numScaleQuestion.validateResponsesDetails(Arrays.asList(resp), 1);
        assertTrue(errors.isEmpty());

        numScaleQuestion.setStep(0.00001);

        ______TS("Test Val=1.33333 and Step=0.00001 does not trigger error");
        resp = new FeedbackNumericalScaleResponseDetails();
        resp.setAnswer(1.33333);
        errors = numScaleQuestion.validateResponsesDetails(Arrays.asList(resp), 1);
        assertTrue(errors.isEmpty());

        ______TS("Test Val=1.333333 and Step=0.00001 triggers error");
        resp = new FeedbackNumericalScaleResponseDetails();
        resp.setAnswer(1.333333);
        errors = numScaleQuestion.validateResponsesDetails(Arrays.asList(resp), 1);
        assertEquals(1, errors.size());
        assertEquals("Please enter a valid value. The two nearest valid values are 1.33333 and 1.33334.",
                errors.get(0));

        numScaleQuestion.setStep(0.7);
        numScaleQuestion.setMinScale(3);
        numScaleQuestion.setMaxScale(5);

        ______TS("Test Val=6 and Max=5,Step=0.7 triggers error");
        resp = new FeedbackNumericalScaleResponseDetails();
        resp.setAnswer(5.1);
        errors = numScaleQuestion.validateResponsesDetails(Arrays.asList(resp), 1);
        assertEquals(1, errors.size());
        assertEquals("5.1 is out of the range for Numerical-scale question.(min=3, max=5)", errors.get(0));

        ______TS("Test Val=0 and Min=3,Step=0.7 triggers error");
        resp = new FeedbackNumericalScaleResponseDetails();
        resp.setAnswer(5.1);
        errors = numScaleQuestion.validateResponsesDetails(Arrays.asList(resp), 1);
        assertEquals(1, errors.size());
        assertEquals("5.1 is out of the range for Numerical-scale question.(min=3, max=5)", errors.get(0));

        ______TS("Test Val=5.1 and Max=5,Step=0.7 triggers error");
        resp = new FeedbackNumericalScaleResponseDetails();
        resp.setAnswer(5.1);
        errors = numScaleQuestion.validateResponsesDetails(Arrays.asList(resp), 1);
        assertEquals(1, errors.size());
        assertEquals("5.1 is out of the range for Numerical-scale question.(min=3, max=5)", errors.get(0));

        FeedbackNumericalScaleResponseDetails correctResp = new FeedbackNumericalScaleResponseDetails();
        correctResp.setAnswer(3);

        FeedbackNumericalScaleResponseDetails respInvalidStep = new FeedbackNumericalScaleResponseDetails();
        respInvalidStep.setAnswer(3.5);

        FeedbackNumericalScaleResponseDetails respInvalidRange = new FeedbackNumericalScaleResponseDetails();
        respInvalidRange.setAnswer(100);

        ______TS("Test 1 correct + 2 wrong triggers right messages");
        errors = numScaleQuestion
                .validateResponsesDetails(Arrays.asList(correctResp, respInvalidStep, respInvalidRange), 1);
        assertEquals(2, errors.size());
        assertEquals("Please enter a valid value. The two nearest valid values are 3.0 and 3.7.", errors.get(0));
        assertEquals("100 is out of the range for Numerical-scale question.(min=3, max=5)", errors.get(1));
    }

    @Test
    public void testValidateQuestionDetails_minShouldBeLessThanMax() {
        FeedbackNumericalScaleQuestionDetails numScaleQuestion = new FeedbackNumericalScaleQuestionDetails();
        List <String> errors = numScaleQuestion.validateErrors(0);
        ______TS("Test Min must be less than Max");
        numScaleQuestion.setMaxScale(1);
        numScaleQuestion.setMinScale(2);
        errors = numScaleQuestion.validateErrors(0);
        assertEquals("Minimum value must be < maximum value for " + "Numerical-scale question" + ".", errors.get(0));
    }

     @Test
    public void testValidateQuestionDetails_shouldBeGreaterThanZero() {
        FeedbackNumericalScaleQuestionDetails numScaleQuestion = new FeedbackNumericalScaleQuestionDetails();
        List <String> errors = numScaleQuestion.validateErrors(0);
        ______TS("Numerical Scale must be greather than 0");
        numScaleQuestion.setStep(0);
        numScaleQuestion.setMaxScale(0);
        numScaleQuestion.setMinScale(-1);
        errors = numScaleQuestion.validateErrors(0);
        assertEquals("Step value must be > 0 for " + "Numerical-scale question" + ".", errors.get(0));
    }

    @Test
    public void testValidateQuestionDetails_outOfTheRange() {
        FeedbackNumericalScaleQuestionDetails numScaleQuestion = new FeedbackNumericalScaleQuestionDetails();
        
        //when(mockArrayList.get(0)).thenReturn(0);
        //List<FeedbackResponseDetails> result = mockArrayList.get("0");
        List <String> errors = numScaleQuestion.validateErrors(0);
        ______TS("Numerical Scale must be in the range");
        numScaleQuestion.setMaxScale(5);
        numScaleQuestion.setMinScale(1);
        errors = numScaleQuestion.validateErrors(0);
        assertEquals("Your Answer is out of the range for " + "Numerical-scale question" + ".", errors.get(0));
    }
}
