package data;

import model.Horoscope;
import java.util.ArrayList;
import java.util.List;

// 더미 데이터
public class HoroscopeData {
    public static List<Horoscope> getDummyData() {
        List<Horoscope> list = new ArrayList<>();

        list.add(new Horoscope(1, "사자자리", "커뮤니케이션 능력이 올라가요. 동료들과의 대화를 즐기세요.", "짙은 녹색의 옷 입기"));
        list.add(new Horoscope(2, "사수자리", "기세의 물결을 탈 기회입니다. 새로운 일을 시작해보세요.", "편지 쓰기"));
        list.add(new Horoscope(3, "양자리", "당신의 평가가 올라가요. 유연한 자세에 신경쓰세요.", "안약넣기"));
        list.add(new Horoscope(4, "천칭자리", "작업이 원활하게 진행돼요. 평소와 다른 방식을 시도하면 좋아요.", "기간 한정의 카페에 가기"));
        list.add(new Horoscope(5, "물병자리", "특가 상품을 만날 수 있어요. 해외여행을 계획 해보는 건 어떨까요?", "요리 동영상 보기"));
        list.add(new Horoscope(6, "쌍둥이자리", "힘이 넘쳐 흐르고 활동적이에요. 오늘은 직감을 믿으세요.", "패션 잡지 읽기"));
        list.add(new Horoscope(7, "전갈자리", "새로운 사랑이 시작될 것 같아요. 아주 조금 용기를 내보세요.", "수중 생물 관찰하기"));
        list.add(new Horoscope(8, "물고기자리", "납득할 수 있는 상품을 손에 넣어요. 천천히 검토하면 좋아요.", "애슬레틱 시설에서 놀기"));
        list.add(new Horoscope(9, "처녀자리", "주제넘은 언동에 주의가 필요해요. 남의 말을 잘 들어요.", "스키야키 먹기"));
        list.add(new Horoscope(10, "게자리", "주변에 휩쓸리기 쉬워요. 자신의 의사를 확실히 표현하세요.", "인형에 리본 두르기"));
        list.add(new Horoscope(11, "염소자리", "인간관계에서 트러블이 발생해요. 존경하는 선배에게 상담해보세요.", "헬스클럽에서 운동하기"));
        list.add(new Horoscope(12, "황소자리", "주의력이 떨어지는 것 같아요. 분실물을 주의해요. 정리정돈은 확실히 하세요.", "빠에야 먹기"));

        return list;
    }
}
