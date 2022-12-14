# Приложение Explore With Me (дополнительное задание)


### [Pull Request](https://github.com/Pa11ady/java-explore-with-me/pull/3)

Explore With Me — приложение, которое дает возможность делиться информацией об интересных событиях и помогает найти компанию для участия в них и оставлять комментарии к событиям.

## Основные возможности приложения

* Мероприятия - Создание событий с различными параметрами (платные/бесплатные, место проведения, дата, количество участников, категория и т.д.).
* Заявки на посещение мероприятия - Можно выбирать события и оставлять заявку на посещение.
* Доступ к информации - Разные возможности по редактированию и получению информации для пользователей и администраторов, модерация событий, фильтрация по параметрам (дата проведения, категория и т.д.)
* Подборки событий - Администраторы могут группировать события по каким-либо признакам, а пользователи знакомиться с подборками.
* Сбор статистики по просмотрам событий для выбора самых популярных.

## Дополнительные возможности приложения

* Пользователи могут оставлять комментарии на событие не зависимо от того началось оно или нет, чтобы выразить своё мнение.
* Комментарии могут быть изменены пользователями только если они не одобрены администратором.
* Пользователи могут удалять любые свои комментарии.
* Пользователи могут видеть только свои комментарии в любом статусе, но чужие комментарии только одобренные администратором.
* Анонимные пользователи могут искать одобренные администратором комментарии по ID
* Анонимные пользователи могут получать комментарии одобренные администратором по ID события
* Администратору доступен расширенный поиск комментариев по пользователям, статусам и дате.
* Администратор может одобрять или отклонять комментарии пользователей.
* Администратор может удалять и редактировать любые комментарии.

## Архитектура

Приложение состоит из двух сервисов:

* Основной сервис — отвечает за обработку информации, которая связана с событиями.
* Сервис статистики — хранит количество просмотров и позволяет делать различные выборки.

## Инструкция

* Собрать проект командой: mvn clean package
* Запустить из каталога проекта под правами администратора: docker-compose up
