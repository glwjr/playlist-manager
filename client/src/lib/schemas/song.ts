import { z } from "zod";

export const songSchema = z.object({
  name: z.string({
    required_error: "Name is required",
    invalid_type_error: "Name must be a string",
  }),
  artist: z.string({
    required_error: "Artist is required",
    invalid_type_error: "Artist must be a string",
  }),
  genre: z.string({
    required_error: "Genre is required",
    invalid_type_error: "Genre must be a string",
  }),
});