/* Renders the phase cards and the program list on the index page.
 *
 * To mark a program as ready, set `ready: true` on it and add a page for it
 * (program-NN.html). Everything else on the site updates from this one list.
 */
(function () {
  'use strict';

  var phases = [
    { id: 1, label: '01', name: 'Getting Started', blurb: 'The JVM, the JDK, and your first compile and run.', level: 1 },
    { id: 2, label: '02', name: 'Core Fundamentals', blurb: 'Variables, control flow, methods, arrays and strings.', level: 1 },
    { id: 3, label: '03', name: 'Object-Oriented Programming', blurb: 'Classes, inheritance, interfaces and composition.', level: 2 },
    { id: 4, label: '04', name: 'Java Essentials', blurb: 'Enums, exceptions, generics and annotations.', level: 2 },
    { id: 5, label: '05', name: 'Collections', blurb: 'Lists, sets, queues and maps, chosen on purpose.', level: 2 },
    { id: 6, label: '06', name: 'Files, I/O and Date', blurb: 'Reading and writing files, and working with time.', level: 2 },
    { id: 7, label: '07', name: 'Modern Java', blurb: 'Lambdas, streams, records and sealed classes.', level: 2 },
    { id: 8, label: '08', name: 'Concurrency', blurb: 'Threads, executors and CompletableFuture.', level: 3 },
    { id: 9, label: '09', name: 'Professional Java', blurb: 'Maven, JUnit 5, JDBC, SOLID and Git.', level: 3 },
    { id: 10, label: 'C', name: 'Capstones', blurb: 'Five systems built from everything above.', level: 3 }
  ];

  function P(no, phase, title, concepts, project, ready, href) {
    return { no: no, phase: phase, title: title, concepts: concepts, project: !!project, ready: !!ready, href: href || '' };
  }

  var programs = [
    P(1, 1, 'Personal Profile Generator', ['javac and java', 'System.out'], true, true, 'program-01.html'),
    P(2, 2, 'Employee Salary Calculator', ['Variables', 'Operators', 'Casting'], false, true, 'program-02.html'),
    P(3, 2, 'Money Handling Mini-Lab', ['BigDecimal', 'Rounding'], false, true, 'program-03.html'),
    P(4, 2, 'Electricity Bill Generator', ['if / else', 'Constants', 'Methods'], false, true, 'program-04.html'),
    P(5, 2, 'ATM Menu Simulation', ['switch', 'Loops', 'Scanner'], false, true, 'program-05.html'),
    P(6, 2, 'Student Marks Analyzer', ['Arrays', 'Method overloading'], false, true, 'program-06.html'),
    P(7, 2, 'Shopping Cart Calculator', ['Strings', 'StringBuilder'], true, true, 'program-07.html'),
    P(8, 3, 'Banking Application', ['Classes', 'Encapsulation'], false, true, 'program-08.html'),
    P(9, 3, 'JUnit Starter Lab', ['JUnit 5', 'Assertions'], false, true, 'program-09.html'),
    P(10, 3, 'Employee Management System', ['Inheritance', 'Polymorphism'], false, true, 'program-10.html'),
    P(11, 3, 'Vehicle Rental System', ['Abstract classes', 'Interfaces'], false, true, 'program-11.html'),
    P(12, 3, 'Payment Processing System', ['Interfaces', 'Composition'], false, true, 'program-12.html'),
    P(13, 3, 'E-commerce Order Management', ['Object relationships'], true, true, 'program-13.html'),
    P(14, 4, 'Order Status Management', ['Enums', 'static', 'final'], false, true, 'program-14.html'),
    P(15, 4, 'Banking Exception Management', ['Custom exceptions', 'try / catch'], false, true, 'program-15.html'),
    P(16, 4, 'Generic Repository', ['Generics', 'Bounded types'], false, true, 'program-16.html'),
    P(17, 4, 'Plugin Discovery Demonstration', ['Annotations', 'Reflection'], true, true, 'program-17.html'),
    P(18, 5, 'Online Shopping Cart', ['List', 'Iterator', 'Comparator'], false, true, 'program-18.html'),
    P(19, 5, 'Customer Search', ['Set', 'Comparable'], false, true, 'program-19.html'),
    P(20, 5, 'Order Processing Queue', ['Queue', 'Deque'], false, true, 'program-20.html'),
    P(21, 5, 'Employee Directory', ['HashMap', 'equals / hashCode'], false, true, 'program-21.html'),
    P(22, 5, 'Product Inventory', ['Map and List together', 'Generics'], true, true, 'program-22.html'),
    P(23, 6, 'Employee File Management', ['Streams', 'Buffered I/O'], false, true, 'program-23.html'),
    P(24, 6, 'Log File Analyzer', ['NIO', 'Path and Files'], false, true, 'program-24.html'),
    P(25, 6, 'Daily Transaction Report', ['LocalDate', 'Formatting'], false, true, 'program-25.html'),
    P(26, 6, 'Appointment Scheduler', ['ZonedDateTime', 'File persistence'], true, true, 'program-26.html'),
    P(27, 7, 'Customer Data Processing', ['Lambdas', 'filter / map']),
    P(28, 7, 'Transaction Reporting', ['Records', 'Optional']),
    P(29, 7, 'Sales Analytics', ['groupingBy', 'Collectors']),
    P(30, 7, 'Product Recommendation Filtering', ['Sealed classes', 'Pattern matching'], true),
    P(31, 8, 'Background Report Generator', ['Threads', 'ExecutorService']),
    P(32, 8, 'Concurrent Order Processor', ['synchronized', 'Race conditions']),
    P(33, 8, 'Parallel Data Processing', ['Future', 'Thread safety']),
    P(34, 8, 'Asynchronous Payment Status Simulator', ['CompletableFuture'], true),
    P(35, 9, 'Maven-based Java Application', ['Maven', 'Project structure']),
    P(36, 9, 'Automated Business Logic Tests', ['JUnit 5', 'Parameterized tests']),
    P(37, 9, 'JDBC Banking Application', ['PreparedStatement', 'Transactions']),
    P(38, 9, 'Employee Database Management', ['CRUD', 'SOLID', 'Git'], true),
    P(39, 10, 'Enterprise Banking System', ['JDBC', 'Collections', 'Tests']),
    P(40, 10, 'E-commerce Order Management', ['Streams', 'Interfaces', 'Database']),
    P(41, 10, 'Employee Management System', ['CRUD', 'Date and time', 'JUnit']),
    P(42, 10, 'Concurrent Order Processing System', ['CompletableFuture', 'Thread safety']),
    P(43, 10, 'Library Management System', ['Solo build', 'Grading rubric'])
  ];

  var levelNames = ['Beginner', 'Intermediate', 'Advanced'];
  var selected = 1;

  function el(tag, className, text) {
    var node = document.createElement(tag);
    if (className) { node.className = className; }
    if (text !== undefined) { node.textContent = text; }
    return node;
  }

  function countFor(phaseId) {
    return programs.filter(function (p) { return p.phase === phaseId; }).length;
  }

  function phaseFromHash() {
    var match = /^#phase-(\d+)$/.exec(window.location.hash);
    if (!match) { return null; }
    var id = parseInt(match[1], 10);
    return phases.some(function (p) { return p.id === id; }) ? id : null;
  }

  function renderPhases() {
    var grid = document.getElementById('phase-grid');
    grid.textContent = '';
    phases.forEach(function (phase) {
      var count = countFor(phase.id);
      var button = el('button', 'phase');
      button.type = 'button';
      button.setAttribute('aria-pressed', String(phase.id === selected));

      var top = el('span', 'top');
      top.appendChild(el('span', 'no', phase.label));
      top.appendChild(el('span', 'count', count + (count === 1 ? ' program' : ' programs')));
      button.appendChild(top);
      button.appendChild(el('span', 'name', phase.name));
      button.appendChild(el('span', 'blurb', phase.blurb));

      button.addEventListener('click', function () {
        selected = phase.id;
        renderPhases();
        renderPrograms();
      });
      grid.appendChild(button);
    });
  }

  function renderPrograms() {
    var phase = phases.filter(function (p) { return p.id === selected; })[0];

    document.getElementById('sel-label').textContent = phase.id === 10 ? 'Capstones' : 'Phase ' + phase.label;
    document.getElementById('sel-name').textContent = phase.name;
    document.getElementById('sel-desc').textContent = phase.blurb;

    var list = document.getElementById('program-list');
    list.textContent = '';

    programs.filter(function (p) { return p.phase === selected; }).forEach(function (program) {
      var row = program.ready ? el('a', 'row') : el('div', 'row is-planned');
      if (program.ready) { row.href = program.href; }

      row.appendChild(el('span', 'c-no', ('0' + program.no).slice(-2)));

      var main = el('span', 'c-main');
      var titleLine = el('span', 'title-line');
      titleLine.appendChild(el('span', 'title', program.title));
      if (program.ready) {
        titleLine.appendChild(el('span', 'pill', 'Ready'));
      } else {
        titleLine.appendChild(el('span', 'pill planned', 'Planned'));
      }
      if (program.project || program.phase === 10) {
        titleLine.appendChild(el('span', 'pill', program.phase === 10 ? 'Capstone' : 'Phase project'));
      }
      main.appendChild(titleLine);

      var chips = el('span', 'chips');
      program.concepts.forEach(function (concept) { chips.appendChild(el('span', 'chip', concept)); });
      main.appendChild(chips);
      row.appendChild(main);

      var level = el('span', 'c-level');
      var dots = el('span', 'dots');
      for (var i = 1; i <= 3; i++) {
        dots.appendChild(el('span', i <= phase.level ? 'dot on' : 'dot'));
      }
      level.appendChild(dots);
      level.appendChild(el('span', '', levelNames[phase.level - 1]));
      row.appendChild(level);

      list.appendChild(row);
    });
  }

  function init() {
    var fromHash = phaseFromHash();
    if (fromHash) { selected = fromHash; }
    renderPhases();
    renderPrograms();
  }

  window.addEventListener('hashchange', function () {
    var fromHash = phaseFromHash();
    if (fromHash) { selected = fromHash; renderPhases(); renderPrograms(); }
  });

  init();
})();
