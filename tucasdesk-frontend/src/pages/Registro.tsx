import React, { useState } from "react";
import { Eye, EyeOff } from "lucide-react";

export default function Register() {
  const [showPassword, setShowPassword] = useState(false);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const togglePassword = () => setShowPassword(!showPassword);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      alert("Passwords do not match!");
      return;
    }
    console.log("Name:", name);
    console.log("Email:", email);
    console.log("Password:", password);
  };

  return (
    <div className="flex h-screen w-screen">
      {/* Left Side */}
      <div className="flex flex-1 justify-center items-center bg-white">
        <form
          onSubmit={handleSubmit}
          className="w-full max-w-sm p-6 flex flex-col gap-4"
        >
          <div className="flex flex-col items-center mb-4">
            <div className="bg-blue-100 p-3 rounded-full mb-2">
              <span className="text-blue-600 font-bold text-xl">R</span>
            </div>
            <h1 className="text-2xl font-bold">Create your account</h1>
            <p className="text-gray-500 text-sm">
              Already have an account? <a href="#" className="text-blue-600">Log in</a>
            </p>
          </div>

          {/* Name */}
          <div>
            <input
              type="text"
              placeholder="Full name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          {/* Email */}
          <div>
            <input
              type="email"
              placeholder="Email address"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          {/* Password */}
          <div className="relative">
            <input
              type={showPassword ? "text" : "password"}
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
            <button
              type="button"
              onClick={togglePassword}
              className="absolute right-3 top-2.5 text-gray-500"
            >
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          </div>

          {/* Confirm Password */}
          <div>
            <input
              type={showPassword ? "text" : "password"}
              placeholder="Confirm password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          {/* Button */}
          <button
            type="submit"
            className="bg-blue-600 text-white rounded-lg py-2 font-medium hover:bg-blue-700 transition"
          >
            Sign up
          </button>

          {/* OR divider */}
          <div className="flex items-center gap-2 text-gray-400 my-2">
            <div className="flex-1 h-px bg-gray-300"></div>
            <span className="text-sm">OR</span>
            <div className="flex-1 h-px bg-gray-300"></div>
          </div>

          {/* Sign up with SSO */}
          <button className="text-blue-600 text-sm">Sign up with SSO</button>
        </form>
      </div>

      {/* Right Side */}
      <div className="hidden md:flex flex-1 bg-blue-950 text-white justify-center items-center">
        <div className="grid grid-cols-3 gap-2 p-6">
          {Array.from({ length: 9 }).map((_, i) => (
            <div
              key={i}
              className="w-20 h-20 bg-blue-600 rounded-tl-full"
            ></div>
          ))}
        </div>
      </div>
    </div>
  );
}
