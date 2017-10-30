ACC.quote = {
	DATA_SUBMIT_BUTTON: 'submitButton',
	DATA_COMMENTS_REQUIRED: 'commentsRequired',
	DATA_MODAL_TITLE_LABEL: 'modalTitleLabel',
	DATA_MODAL_INPUT_LABEL: 'modalInputLabel',
	DATA_MODAL_COMMENT_REQUIRED: 'modalCommentRequired',
	quoteDecisionSubmissionEnabled: false,

	_autoload: [
		"bindQuoteCommentModal"
	],

	bindQuoteCommentModal: function()
	{
		$(document).on('click','.quoteOrderDecisionForm .decisionButton' ,function(e) {
			e.preventDefault();

			var form = $(this).closest('form');
			var title = $(this).data(ACC.quote.DATA_MODAL_TITLE_LABEL);
			var inputLabel = $(this).data(ACC.quote.DATA_MODAL_INPUT_LABEL);
			var commentRequired = $(this).data(ACC.quote.DATA_MODAL_COMMENT_REQUIRED);
			var quoteCommentModal = form.find('.quoteCommentModal');

			// Cannot be initialized in onOpen
			ACC.quote.initQuoteCommentModal(quoteCommentModal, inputLabel, commentRequired);

			ACC.colorbox.open(title, {
				href: quoteCommentModal,
				inline: true,
				width: '620px',
				onOpen: function () {
				},
				onComplete: function () {
				},
				onClosed: function () {
					if (ACC.quote.quoteDecisionSubmissionEnabled) {
						form.submit();
					}
				}
			});
		});

		$(document).on('click','.quoteCommentModal .submitQuoteCommentButton',function(e) {
			e.preventDefault();

			ACC.quote.quoteDecisionSubmissionEnabled = true;
			ACC.colorbox.close();
		});

		$(document).on('click','.quoteCommentModal .cancelQuoteCommentButton',function(e) {
			e.preventDefault();

			ACC.colorbox.close();
		});

		$('.quoteCommentModal textarea[name=comments]').keyup(function() {
			var submitButton = $(this).data(ACC.quote.DATA_SUBMIT_BUTTON);

			if (submitButton.data(ACC.quote.DATA_COMMENTS_REQUIRED) === true)
			{
				submitButton.prop('disabled', this.value == "" ? true : false);
			}
		});
	},

	initQuoteCommentModal: function(quoteCommentModal, inputLabel, disableSubmit)
	{
		var comments = quoteCommentModal.find('textarea[name=comments]');
		var commentsLabel = quoteCommentModal.find('.headline');
		var submitButton = quoteCommentModal.find('.submitQuoteCommentButton');

		comments.val('');
		comments.data(ACC.quote.DATA_SUBMIT_BUTTON, submitButton);

		commentsLabel.text(inputLabel);

		submitButton.prop('disabled', disableSubmit);
		submitButton.data(ACC.quote.DATA_COMMENTS_REQUIRED, disableSubmit);

		ACC.quote.quoteDecisionSubmissionEnabled = false;
	}
};
