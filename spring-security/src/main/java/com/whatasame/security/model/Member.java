package com.whatasame.security.model;

import java.util.List;

public record Member(long id, String email, String password, List<String> roles) {}
