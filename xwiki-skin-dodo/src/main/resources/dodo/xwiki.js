var XWiki = {
  Version: '0.8_pre1',
  require: function(libraryName) {
    // inserting via DOM fails in Safari 2.0, so brute force approach
    document.write('<script type="text/javascript" src="'+libraryName+'"></script>');
  },
  addLibrary: function(scriptLibraryName) {
    JSfileName = 'xwiki.js'; // This should be added in a xwiki.js file 'xwiki.js'
    if(scriptLibraryName=='scriptaculous') {
	libraries = ['prototype.js', 'util.js', 'effects.js', 'dragdrop.js', 'controls.js'];
    }
    if(scriptLibraryName=='rico') {
	libraries = ['rico.js'];
    }
    var scriptTags = document.getElementsByTagName("script");
    for(var i=0;i<scriptTags.length;i++) {
      if(scriptTags[i].src && scriptTags[i].src.match(JSfileName)) {
        var path = scriptTags[i].src.replace(JSfileName,scriptLibraryName) + '/';
	  for (var j=0;j<libraries.length;j++) {
	    this.require (path + libraries[j]);
	  }
      }
    }
  }
};

var XWikiAjax = {
  requests: new Array(),
  start: function(status) {
    this.status = $(status);
    ajaxEngine.registerRequest ('setValue', 'SetValueResponse?xpage=rdf');
    ajaxEngine.registerAjaxElement (this.status.id);
  },
  addRequest: function(dName, cName, field, value) {
    var request = Object.extend({
      type: 'set'
    });
    request.className = cName;
    request.document = dName;
    request.field = field;
    request.value = value;
    this.requests.push(request);
  },
  end: function() {
    this.requests.reverse();
    var req = this.requests.pop();
    while (req) {
      if (req.type=='set') {
        ajaxEngine.sendRequest ('setValue', 'status='+this.status.id, 'doc='+req.document, 
	    'typedoc='+req.className, 'field='+req.field, 'value='+req.value);
      };
	req = this.requests.pop();
    }
    this.status.innerHtml = 'updated';
  }
};

function updateName(field1, field2, removeclass) {
  var name = field1.value;
  name = name.replace(/[���]/gi,"a")
  name = name.replace(/[����]/gi,"e")
  name = name.replace(/[��]/gi,"i")
  name = name.replace(/[��]/gi,"o")
  name = name.replace(/[���]/gi,"u")
  name = name.replace(/["!?]/g,"");
  name = name.replace(/[_':,;]/g," ");
  name = name.replace(/[\.]/g,"");
  if (removeclass!=false)
   name = name.replace(/class$/gi,"");
  if (navigator.userAgent.indexOf("Safari")==-1) {
   name = name.replace(/\s(.)/g,function(str, p1) { return p1.toUpperCase(); });
   name = name.replace(/^(.)/g,function(str, p1) { return p1.toUpperCase(); });
  }
  name = name.replace(/\s+/g,"");
  if (field2 == null) {
         field1.value = name;
     } else {
         field2.value = name;
     }
  if (name=="")
   return false;
  return true;
}

function noaccent(chaine) {
  temp = chaine.replace(/[���]/gi,"a")
  temp = temp.replace(/[����]/gi,"e")
  temp = temp.replace(/[��]/gi,"i")
  temp = temp.replace(/[��]/gi,"o")
  temp = temp.replace(/[���]/gi,"u")
  temp = temp.replace(/["!?]/g,"");
  temp = temp.replace(/[_':]/g," ");
  if (navigator.userAgent.indexOf("Safari")==-1) {
  temp = temp.replace(/\s(.)/g,function(str, p2) { return p2.toUpperCase(); });
  temp = temp.replace(/^(.)/g,function(str, p2) { return p2.toUpperCase(); });
  }
  temp = temp.replace(/\s+/g,"");
  return temp;
}
function prepareName(form) {
 var fname = form.register_first_name.value;
 var lname = form.register_last_name.value;
 var cxwikiname = form.xwikiname;
 if (fname != "") {
   fname = fname.substring(0,1).toUpperCase() + fname.substring(1);
   fname.replace(/ /g,"");
 }
 if (lname != "") {
   lname = lname.substring(0,1).toUpperCase() + lname.substring(1)
   lname.replace(/ /g,"");
 }
 if (cxwikiname.value == "") {
   cxwikiname.value  =  noaccent(fname + lname);
 }
}

Ajax.XWikiRequest = Class.create();

Object.extend(Object.extend(Ajax.XWikiRequest.prototype, Ajax.Request.prototype), {
  initialize: function(space, docName, options, action) {

    this.transport = Ajax.getTransport();
    this.setOptions(options);
    if (action)
      this.action = action;
    else
      this.action = "view";
    this.baseUrl = "/xwiki/bin/" + action;

    var onComplete = this.options.onComplete || Prototype.emptyFunction;
    this.options.onComplete = (function() {
      this.returnValue(onComplete);
      //onComplete(this.transport);
    }).bind(this);

    this.request(this.generateUrl(space, docName));
  },

    generateUrl: function(space, docName){
        return this.baseUrl + "/" + space + "/" + docName;
  },

  returnValue: function(callBack) {

    if (callBack)
        callBack(this.transport);
    else
        alert("error, callback");
  }
});



var XWiki = Class.create();

XWiki.prototype = {
        initialize: function(wikiUrl){this.wikiUrl = wikiUrl;},
        getSpaces: function(callBack){
            var params = '';
            var myAjax = new Ajax.XWikiRequest( "Ajax", "getSpaces", {method: 'get', parameters: params, onComplete: getSpacesCallBack} );
        },

        getSpacesCallBack: function(ajaxResponse){
            var xml = ajaxResponse.responseXML;

        }
}

