var localStorage = window.localStorage;

function handleAdd() {
  var todoText = document.getElementById('todo-text').value;
  if (!isEmpty(todoText))
    saveTask(todoText).then(json => {
      addTask(todoText);
      document.getElementById('todo-text').value = '';
    })
}

function addTask(taskText) {
  var rows = document.getElementById("tbody").rows.length;
  if (rows === 5) {
    removeTableLastRow();
    addTotalPage();
  }
  addTableRow(taskText);
}

function addTableRow(taskText) {
  var tableRef = document.getElementById('task-table').getElementsByTagName('tbody')[0];
  var row   = tableRef.insertRow(0);
  var taskTextCell = row.insertCell(0);
  var editCell     = row.insertCell(1);
  var deleteCell   = row.insertCell(2);
  taskTextCell.appendChild(document.createTextNode(taskText));
  editCell.appendChild(document.createTextNode('Edit'));
  editCell.addEventListener('click', this.showEditForm, false);
  deleteCell.appendChild(document.createTextNode('Delete'));
  deleteCell.addEventListener('click', this.handleDeleteRow, false);
}

function removeTableLastRow() {
    var table = document.getElementById('tbody');
    var rowCount = table.rows.length;
    table.deleteRow(rowCount -1);
}

function clearTable() {
  var tbody = document.getElementById('tbody').innerHTML = '';
}

function handleDeleteRow() {
  var row = this.parentElement.rowIndex;
  var taskText = this.parentElement.firstChild.outerText;
  remove(BASE_URL+'/users/me/tasks/?taskText='+taskText)
  document.getElementById("task-table").deleteRow(row);
  var rows = document.getElementById('tbody').rows.length;
  if (rows === 4) {
    clearTable();
    loadUserTasks();
  }
}

function saveTask(taskText) {
  return post(BASE_URL+'/users/me/tasks', {text: taskText});
}

function showEditForm() {
  document.getElementById('update-task-form').style.display='block';
  localStorage.setItem('taskToChange', this.parentElement.firstChild.outerText)
}

function closeEditForm() {
  document.getElementById('update-task-form').style.display='none';
  localStorage.removeItem('taskToChange');
  document.getElementById('title-input').value = '';
}

function handleUpdateTask() {
  var newTitle = document.getElementById('title-input').value;
  if (!(isEmpty(newTitle))) {
    var row = findRowByTask(localStorage.getItem('taskToChange'));
    row.firstChild.textContent = newTitle;
    closeEditForm();
  }
}

function findRowByTask(taskText) {
  var rows = document.getElementById('tbody').rows;
  for (var i = 0; i < rows.length; i++) {
    var value = rows[i].firstChild.textContent;
    if (value === taskText) {
      return rows[i];
    }
  }
}

function handleCloseUpdateForm() {
  closeEditForm();
}
