package engine;

/**
 * 플레이어 목숨이 1일 때 적 총알 속도를 절반으로 낮추는 임시 난이도 조절기
 *
 * 복구 조건 (둘 중 하나):
 *  1) 난이도가 낮아진 상태에서 3레벨 이상 클리어
 *  2) 목숨이 2 이상으로 회복
 *
 * GameScreen은 레벨마다 새로 생성되므로 싱글톤으로 상태를 유지한다.
 */
public class DifficultyAdjustWhilePlaying {

   

    private static final DifficultyAdjustWhilePlaying INSTANCE =
            new DifficultyAdjustWhilePlaying();

    private boolean tooLowLives;               // 현재 목숨이 1 이하일 때 true
    private boolean ClearedEnoughLevels;       // 난이도가 낮아진 상태에서 3레벨 이상 클리어 했을 때 true
    private int clearedLevelsWhileActive;      // 난이도가 낮아진 상태에서 클리어한 레벨 수

    private DifficultyAdjustWhilePlaying() {
    }

    public static DifficultyAdjustWhilePlaying getInstance() {
        return INSTANCE;
    }

    /** 새 게임을 시작할 때 호출해 상태를 초기화한다. */
    public void reset() {
        tooLowLives = false;
        ClearedEnoughLevels = false;
        clearedLevelsWhileActive = 0;
    }

    /** 매 프레임 현재 목숨을 넘겨서 발동/복구를 판단한다. */
    public void update(int livesRemaining) {
        if (livesRemaining >= 2) {
            // 목숨 회복으로 복구
            tooLowLives = false;
            ClearedEnoughLevels = false;
            clearedLevelsWhileActive = 0;
        } else if (livesRemaining <= 1 && livesRemaining > 0
                && !tooLowLives && !ClearedEnoughLevels) {
            tooLowLives = true;
            clearedLevelsWhileActive = 0;
        }
    }

    /** 레벨을 클리어했을 때 호출한다 (목숨이 남은 상태에서). */
    public void onLevelCleared() {
        if (!tooLowLives) {
            return; //플레이어의 목숨이 2 이상일 때는 작동하지 않음
        }
        clearedLevelsWhileActive++;
        if (clearedLevelsWhileActive >= 3) {
           
            ClearedEnoughLevels = true; // 목숨이 아직 1이어도 3레벨 이상 클리어 했을 시 난이도 조절기가 꺼짐
            clearedLevelsWhileActive = 0;
        }
    }

    /** 적 총알의 기본 속도를 넣으면 현재 상태에 맞는 속도를 돌려준다. */
    public int adjustEnemyBulletSpeed(int baseSpeed) {
        if (tooLowLives&&!ClearedEnoughLevels) {
            return  (int) (baseSpeed * 0.5);
        }
            return baseSpeed;
    }

    
}