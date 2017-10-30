ACC.customerticketingaddon = {

	_autoload: [
		"onStatusChange",
		"bindMessageArea",
		"toggleAllMessages",
		"postNewMessage",
		"onValidationErrors",
		"onFileChosen"
	],


	disableMessage: function(_this){
		var currentTiecketStatus = $('input[id="currentTicketStatus"]').val();
		var selectedStatus = $(_this).val();
		
		if(currentTiecketStatus != selectedStatus) {
			$('.cs_file_upload').hide();
		} else {
			$('.cs_file_upload').show();
		}
		
		if(currentTiecketStatus == 'COMPLETED' && selectedStatus == 'COMPLETED') {
			$('textarea[id="message"]').attr('disabled','disabled');
			$('button[id="updateTicket"]').attr('disabled','disabled');
		} else {
			$('textarea[id="message"]').removeAttr('disabled');
		}
	},

	onStatusChange: function () {
        $(document).on('change', '.js-add-message-status', function () {
            ACC.customerticketingaddon.disableMessage(this);
		});
	},

	onFileChosen: function () {
        $(document).on('change', '#supportTicketForm input[name=files]', function () {
            var limit = 10485760;
            var size = 0;
            for (var i = 0; i < this.files.length; ++i)
            {
                size += this.files[i].size;
            }
            if (size > limit)
            {
                $("#supportTicketForm .js-file-upload__file-name").html("<span style='color:red'>File is too big (>10mb)</span>");
                this.value="";
            }
        });
    },

    bindMessageArea: function () {
        $(document).on('keyup', '.js-add-message', function () {
            if($(this).val().length > 0) {
                $('button[id="updateTicket"]').removeAttr('disabled');
            } else {
                $('button[id="updateTicket"]').attr('disabled','disabled');
            }
        });
    },

	toggleAllMessages: function() {
		$('#ct-toggle-all-messages').on('click touchstart', function() {
			$('.cts-msg-history-item:not(.ct-msg-visible)').show();
			$(this).hide();
		});
	},

	postNewMessage: function () {
		var title = $('#ct-overlay-title').html();
		var content = $('#ct-add-new-msg').html();
		$('.ct-add-new-msg-btn').on('click touchstart', function(e) {
			e.preventDefault();
			$.colorbox({
				href: "#ct-add-new-msg",
				maxWidth:"100%",
				width: 525,
				opacity:0.7,
				title: title,
				html: content,
				close:'<span class="glyphicon glyphicon-remove"></span>',
				onComplete: function(){
                    ACC.customerticketingaddon.disableMessage($('.js-add-message-status'));
                    $('button[id="updateTicket"]').attr('disabled','disabled');
                    ACC.csvimport.changeFileUploadAppearance();
                }
			});
		})
	},
	
	onValidationErrors: function() {
		var title = $('#ct-overlay-title').html();
		var content = $('#ct-add-new-msg').html();
		if( $('.updateSupportTicketValidationErrors').length) {
			$.colorbox({
				href: "#ct-add-new-msg",
				maxWidth:"100%",
				width: 525,
				opacity:0.7,
				title: title,
				html: content,
				close:'<span class="glyphicon glyphicon-remove"></span>',
				onComplete: function(){
                    ACC.customerticketingaddon.disableMessage($('.js-add-message-status'));
                    $('button[id="updateTicket"]').attr('disabled','disabled');
				}
			});
		}
	}
};