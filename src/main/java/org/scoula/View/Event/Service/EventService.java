package org.scoula.View.Event.Service;

import org.scoula.news.service.NewsService;
import org.scoula.user.service.UserService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

	private final UserService userService;
	private final NewsService newsService;


}
