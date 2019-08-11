class Pagination {
  constructor(resourseName, itemsPerPage, sortBy) {
    this.resourseName = resourseName;
    this.itemsPerPage = itemsPerPage;
    this.sortBy = sortBy;
    this.pageNumber = 1;
    this.totalPages = 1;
  }

  loadPage() {
    return get(BASE_URL+this.resourseName+'?page='+(this.pageNumber-1)+
      "&size="+this.itemsPerPage+'&sort='+this.sortBy)
      .then(json => {
        this.totalPages = json.totalPages;
        return json;
      });
  }

  nextPage() {
    if(!(this.totalPages === this.pageNumber || this.totalPages === 0)) {
      this.pageNumber++;
      return true;
    }
    return false;
  }

  currentPage() {
    return this.pageNumber;
  }

  previousPage() {
    if (!(this.pageNumber === 1)) {
      this.pageNumber--;
      return true;
    }
    return false;
  }

  addTotalPage() {
    this.totalPages++;
  }
}

var tasksPagination = new Pagination('/users/me/tasks', 5, 'createDate');

function loadUserTasks() {
  tasksPagination.loadPage()
    .then(json => {
      setCurrentPageNumber();
      json.content.forEach(t => addTableRow(t.text));
    });
}

function nextPage() {
  if(tasksPagination.nextPage()) {
    clearTable();
    loadUserTasks();
    setCurrentPageNumber();
  }
}

function previousPage() {
  if(tasksPagination.previousPage()) {
    clearTable();
    loadUserTasks();
    setCurrentPageNumber();
  }
}

function addTotalPage() {
  tasksPagination.addTotalPage();
}

function setCurrentPageNumber() {
  document.getElementById('page').textContent = tasksPagination.currentPage();
}

function currentPage() {
  return tasksPagination.currentPage();
}
