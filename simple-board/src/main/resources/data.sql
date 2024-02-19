INSERT INTO article(id, title, content) VALUES(1, '가가가가', 'ㄱㄱㄱㄱ');
INSERT INTO article(id, title, content) VALUES(2, '나나나나', 'ㄴㄴㄴㄴ');
INSERT INTO article(id, title, content) VALUES(3, '다다다다', 'ㄷㄷㄷㄷ');

-- 더미 Article 생성
INSERT INTO article(id, title, content) VALUES(4, '영화추천', '댓글ㄱ');
INSERT INTO article(id, title, content) VALUES(5, '음식추천', '댓글ㄱㄱ');
INSERT INTO article(id, title, content) VALUES(6, '게임추천', '댓글ㄱㄱㄱ');

-- 더미 Comment 생성
INSERT INTO comment(id, article_id, nickname, body) VALUES(1, 4, 'PARK', '차우');
INSERT INTO comment(id, article_id, nickname, body) VALUES(2, 4, 'NAM', '파묘');
INSERT INTO comment(id, article_id, nickname, body) VALUES(3, 4, 'LEE', '곡성');

INSERT INTO comment(id, article_id, nickname, body) VALUES(4, 5, 'PARK', '치킨');
INSERT INTO comment(id, article_id, nickname, body) VALUES(5, 5, 'NAM', '피자');
INSERT INTO comment(id, article_id, nickname, body) VALUES(6, 5, 'LEE', '초밥');

INSERT INTO comment(id, article_id, nickname, body) VALUES(7, 6, 'PARK', '롤');
INSERT INTO comment(id, article_id, nickname, body) VALUES(8, 6, 'NAM', '피파');
INSERT INTO comment(id, article_id, nickname, body) VALUES(9, 6, 'LEE', '서든');
