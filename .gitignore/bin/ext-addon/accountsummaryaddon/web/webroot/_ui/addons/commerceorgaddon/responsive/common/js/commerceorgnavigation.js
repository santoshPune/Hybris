ACC.myCompanyNavigation = {

    _autoload: [
        "myCompanyNavigation"
    ],


    myCompanyNavigation: function(){


        var aCompData = [];
        var oMyCompanyData = $('.companyNavComponent');


        //the my company hook for the desktop

        var oMMainNavDesktopCom = $(".js-secondaryNavCompany .js-nav__links");

        if(oMyCompanyData){
            var cLinks = oMyCompanyData.find("a");
            for(var i = 0; i < cLinks.length; i++){
                aCompData.push({link: cLinks[i].href, text: cLinks[i].title});
            }
        }

        //create Welcome User + expand/collapse and close button
        //This is for mobile navigation. Adding html and classes.
        var oUserInfo = $(".js-logged_in");
        //Check to see if user is logged in
        if(oUserInfo && oUserInfo.length === 1 && oMyCompanyData.length > 0)
        {


            //FOR DESKTOP

            //FOR MOBILE
            //create a My Company Top link for desktop - in case more components come then more parameters need to be passed from the backend

            //add my company icon
            //<span class="glyphicon glyphicon-list-alt"></span>
            var myCompanyHook = $('<a class=\"myCompanyLinksHeader collapsed js-myCompany-toggle\"  data-toggle=\"collapse\" data-parent=".nav__right" href=\"#accNavComponentDesktopTwo\">' + oMyCompanyData.data("title") + '</a>' );
            myCompanyHook.insertBefore(oMyCompanyData);


            $('.js-userAccount-Links').append($('<li class="auto"><div class="myCompanyLinksContainer js-myCompanyLinksContainer"></div></li>'));
            var myCompanyHook = [];
            myCompanyHook.push('<div class="sub-nav">')
            myCompanyHook.push('<a id="signedInCompanyToggle" class="myCompanyLinksHeader collapsed js-myCompany-toggle"  data-toggle="collapse" data-target=".offcanvasGroup3">');
            myCompanyHook.push(oMyCompanyData.data("title"));
            myCompanyHook.push('<span class="glyphicon glyphicon-chevron-down myCompanyExp"></span>');
            myCompanyHook.push('</a>');
            myCompanyHook.push('</div>');
            $('.js-myCompanyLinksContainer').append(myCompanyHook.join(''));

            //add UL element for nested collapsing list
            $('.js-myCompanyLinksContainer').append($('<ul data-trigger="#signedInCompanyToggle" class="js-myCompany-root offcanvasGroup3 offcanvasNoBorder subNavList js-nav-collapse-body  collapse sub-nav "></ul>'));

            //My Company links for desktop

            for(var i = aCompData.length - 1; i >= 0; i--){
                var oLink = oDoc.createElement("a");
                oLink.title = aCompData[i].text;
                oLink.href = aCompData[i].link;
                oLink.innerHTML = aCompData[i].text;

                var oListItem = oDoc.createElement("li");
                oListItem.appendChild(oLink);
                oListItem = $(oListItem);
                oListItem.addClass("auto ");
                $('.js-myCompany-root').append(oListItem);
            }

        }
        //desktop

        for(var i = 0; i < aCompData.length; i++){
            var oLink = oDoc.createElement("a");
            oLink.title = aCompData[i].text;
            oLink.href = aCompData[i].link;
            oLink.innerHTML = aCompData[i].text;

            var oListItem = oDoc.createElement("li");
            oListItem.appendChild(oLink);
            oListItem = $(oListItem);
            oListItem.addClass("auto col-md-4");
            oMMainNavDesktopCom.get(0).appendChild(oListItem.get(0));
        }

        //hide and show contnet areas for desktop
        $('.js-secondaryNavAccount').on('shown.bs.collapse', function () {

            if($('.js-secondaryNavCompany').hasClass('in')){
                $('.js-myCompany-toggle').click();
            }

        });

        $('.js-secondaryNavCompany').on('shown.bs.collapse', function () {

            if($('.accNavComponentAccount').hasClass('in')){
                $('.js-myAccount-toggle').click();
            }

        });

    }

};