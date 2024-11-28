import { NextResponse } from "next/server";

export const GET = async function (req: Request) {
  try {
    const res = await fetch("http://localhost:8080/api/songs");
    const songs = await res.json();

    return NextResponse.json(songs);
  } catch (error: any) {
    return NextResponse.json(
      { error: error.message },
      { status: error.status || 500 }
    );
  }
};