filtersModule3.filter('searchDateFilter', function () {
    return function (items) {
        var filtered = [];
        if (this.searchDate) {
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                var tempDate = new Date(item.dateLog);
                var tempStringDate = tempDate.getFullYear() + '-' + this.totalwlog.leadingZero((tempDate.getMonth()+1));
                if (tempStringDate == this.searchDate) {
                    filtered.push(item);
                }
            }
            this.filteredWorklogs = filtered;
            return filtered;
        } else
            return items;
    };
});