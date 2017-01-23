package com.github.dmitriylamzin.translator.service;

import com.github.dmitriylamzin.dto.Bitcoin;

public interface Observer {
  void update(Bitcoin bitcoin);
}
