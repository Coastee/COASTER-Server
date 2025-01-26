package com.coastee.server.fixture;

import com.coastee.server.server.domain.Server;

import java.util.List;

public class ServerFixture {
    private static List<String> serverTitleList = List.of(
            "광고/마케팅",
            "부동산/건설",
            "인사/법률",
            "푸드/농업",
            "교육",
            "뷰티/화장품",
            "제조/하드웨어",
            "환경/에너지",
            "금융/보험/핀테크",
            "AI/딥테크/블록체인",
            "커머스/비즈니스",
            "홈리빙/펫",
            "게임",
            "소셜미디어/커뮤니티",
            "콘텐츠/예술",
            "헬스케어/바이오",
            "모빌리티/교통",
            "여행/레저",
            "통신/데이터",
            "스포츠",
            "물류",
            "유아/출산",
            "패션"
    );

    public static List<Server> getAll() {
        return serverTitleList.stream().map(Server::new).toList();
    }
}
