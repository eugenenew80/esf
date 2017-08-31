package esf.service;

import java.util.List;

import esf.entity.Responsibility;

public interface AuthService {
	Long auth(String userName, String passworf);
	List<Responsibility> getResponsibility(Long userId);
}
