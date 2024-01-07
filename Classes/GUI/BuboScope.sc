Scope {

  *new {
    ^Server.default.scope.window.alwaysOnTop_(true);
  }

}

Meter {

  *new {
    var window = Window.new(
      "Meter",
      Rect.new(left: 0, top: 0, width: 680, height: 250),
      resizable: false,
      border: true,
      scroll: false
    );
    var meters = ServerMeterView.new(
    Server.default, window,
      0@0, 16, 16
    );
    window.front;
    window.alwaysOnTop = true;
    ^window
  }

}
