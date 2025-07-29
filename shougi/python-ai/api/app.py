# shougi/python-ai/api/app.py

# アプリ起動前に起動する
#  cd C:\Users\NTB015\OneDrive\デスクトップ\shougi\python-ai\api←パス
# uvicorn app:app --reload --port 8000

from fastapi import FastAPI, Request
import shogi

app = FastAPI()

@app.post("/legal_moves")
async def get_legal_moves(data: dict):
    print('run api')
    sfen = data["sfen"]
    board = shogi.Board(sfen)
    move_map = {}
    for move in board.legal_moves:
        if move.from_square is not None:
            f = shogi.SQUARE_NAMES[move.from_square]
            t = shogi.SQUARE_NAMES[move.to_square]
            key = f"{f}{'+' if move.promotion else ''}"  # 成る手には "+" を追加
            move_map.setdefault(key, []).append(t)
        else:
            piece = move.drop_piece_type
            t = shogi.SQUARE_NAMES[move.to_square]
            move_map.setdefault("hand_" + shogi.PIECE_SYMBOLS[piece], []).append(t)
    return move_map