(function () {
    angular.module("common")
        .factory("descriptionServiceHelper", function ($filter) {
            return {

                //Creating fields
                createFieldsFrom: function (fields, names, attrs) {
                    var newFields = [];
                    angular.forEach(fields, function (field) {
                        if ($filter('filter')(names, field.name, true).length > 0) {

                            var newField = angular.copy(field);
                            if (attrs) {
                                angular.forEach(newField.controls, function (control) {
                                    var attr = $filter('filter')(attrs, {name: control.name}, true)[0];

                                    if (attr) {
                                        control.editable = attr.editable;
                                        control.post = attr.post;
                                        control.visible = attr.visible;
                                        control.required = attr.required;
                                        control.control = attr.control || control.control;

                                        if (!attr.autofill && control.autofill)
                                            delete control.autofill;

                                        control.postedName = attr.postedName || control.postedName || control.name;
                                    }
                                })
                            }

                            newFields.push(newField);
                        }
                    });

                    return newFields;
                },                
                
                beforePostItem: function (item, fields) {
                    
                	if (item.formTypeIds) {
                		item.formTypeId = [];
                		angular.forEach(item.formTypeIds, function(value) {
                			if (value.checked)
                				item.formTypeId.push(value.id);
                		})
                	}
                	
                	angular.forEach(item, function (value, key) {
                        var isPost = false;
                        angular.forEach(fields, function (field) {
                            if ($filter('filter')(field.controls, {name: key, post: true}, true).length > 0)
                                isPost = true;
                        });

                        if (!isPost)
                            delete item[key];
                    });

                    return item;
                },

                afterGetItem: function (item, fields) {
                    for (i = 0; i < fields.length; i++) {
                        for (j = 0; j < fields[i].controls.length; j++) {

                            var fieldName = fields[i].controls[j].name;
                            var fieldType = fields[i].controls[j].dataType;

                            if (fieldType == "date" && item[fieldName])
                                item[fieldName] = new Date(item[fieldName]);

                            if (fieldType == "integer" && item[fieldName])
                                item[fieldName] = parseInt(item[fieldName]);

                            if (fieldType == "number" && item[fieldName])
                                item[fieldName] = parseFloat(item[fieldName]);
                        }
                    }

                    return item;
                }                
            }
        })
})();