public class RamenProgram {
    public static void main(String[] args) {
        try {
            // 0인경우 프로그램 종료
            if (args.length == 0) {
                System.out.println("인자가 1이상 이어야 합니다.");
                return;
            }
            // 개수만큼 객체 생성
            RamenCook ramenCook = new RamenCook(Integer.parseInt(args[0]));
            // 4개의 스레드
            new Thread(ramenCook, "A").start();
            new Thread(ramenCook, "B").start();
            new Thread(ramenCook, "C").start();
            new Thread(ramenCook, "D").start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class RamenCook implements Runnable {
    private int ramenCount;  // 남은 라면의 개수를 저장
    private String[] burners = {"_", "_", "_", "_"};  // 버너 상태를 저장하는 배열

    // 생성자: 초기 라면 개수를 설정합니다.
    public RamenCook(int count) {
        ramenCount = count;  // 라면 개수를 초기화
    }

    // 스레드 실행 코드
    @Override
    public void run() {
        while (ramenCount > 0) {
            synchronized (this) {
                // 현재 상태를 출력합니다. 라면의 개수가 0 이하라면 종료
                if (ramenCount <= 0) break;
                ramenCount--;  // 라면 개수를 하나 줄입니다.
                System.out.println(
                        Thread.currentThread().getName()
                                + ": " + ramenCount + "개 남음");
            }

            // 배열을 순회하며 사용 가능한 것을 찾는다.
            for (int i = 0; i < burners.length; i++) {
                if (!burners[i].equals("_")) continue;  // 사용 중인 버너 pass

                // 현재 스레드 남은 버너를 사용
                synchronized (this) {

                    if (burners[i].equals("_")) {
                        burners[i] = Thread.currentThread().getName();
                        System.out.println(
                                "\t\t\t\t"
                                        + Thread.currentThread().getName()
                                        + ": [" + (i + 1) + "]번 버너 ON");
                        showBurners();
                    }
                }
                try {
                    Thread.sleep(2000);  // 2초 동안 딜레이
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 사용을 마친 경우
                synchronized (this) {
                    // 버너를 사용하지 않는 상태로 전환
                    burners[i] = "_";
                    System.out.println(
                            "\t\t\t\t"
                                    + Thread.currentThread().getName()
                                    + ": [" + (i + 1) + "]번 버너 OFF");
                    showBurners();
                }
                break;  // 사용을 마침으로 종료
            }
            try {
                // 랜덤한 시간 동안 대기 (초단위)
                Thread.sleep(Math.round(1000 * Math.random()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 현재 버너 상태 출력
     */
    private void showBurners() {
        String stringToPrint = "\t\t\t\t";
        for (int i = 0; i < burners.length; i++) {
            stringToPrint += (" " + burners[i]);  // 각 버너의 상태를 추가.
        }
        System.out.println(stringToPrint);
    }
}
