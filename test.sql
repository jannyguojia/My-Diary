-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 15, 2023 at 07:39 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `test`
--

-- --------------------------------------------------------

--
-- Table structure for table `accountinfo`
--

CREATE TABLE `accountinfo` (
  `ID` int(11) NOT NULL,
  `DiaryName` text NOT NULL,
  `UserName` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `Email` text NOT NULL,
  `Password` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `accountinfo`
--

INSERT INTO `accountinfo` (`ID`, `DiaryName`, `UserName`, `Email`, `Password`) VALUES
(1003, 'Jia\'sDiary', 'jia', 'jia@sina.com', 'e10adc3949ba59abbe56e057f20f883e'),
(1004, 'qwe', 'qwe', 'qwe@sina.com', '76d80224611fc919a5d54f0ff9fba446'),
(1005, 'Emma', 'emma', 'emma@sina.com', '202cb962ac59075b964b07152d234b70'),
(1006, 'Anmeng', 'an', 'an@qq.com', 'e10adc3949ba59abbe56e057f20f883e'),
(1007, 'DA', 'dafei', 'dafei@qq.com', '202cb962ac59075b964b07152d234b70'),
(1008, 'DUDU', 'du', 'du@gmail.com', 'b247deafa97a5122eef246b489074c5d'),
(1009, 'GUO', 'guo', 'guo@qq.com', 'e10adc3949ba59abbe56e057f20f883e'),
(1010, 'sss', 'sss', 'sss@qq.com', '202cb962ac59075b964b07152d234b70'),
(1011, 'yu', 'guoyu', 'guoyu@sina.com', '202cb962ac59075b964b07152d234b70'),
(1012, 'jia', 'jia', 'jia@jia.com', 'a6907acf5b337a322193f19b6698c867');

-- --------------------------------------------------------

--
-- Table structure for table `diaryinfo`
--

CREATE TABLE `diaryinfo` (
  `No` int(11) NOT NULL,
  `ID` int(11) NOT NULL,
  `Color` text NOT NULL,
  `Encryption` text NOT NULL,
  `Date` text NOT NULL,
  `EncryptDiary` text NOT NULL,
  `KeyName` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `diaryinfo`
--

INSERT INTO `diaryinfo` (`No`, `ID`, `Color`, `Encryption`, `Date`, `EncryptDiary`, `KeyName`) VALUES
(1, 1012, 'Blue', 'AES', '2023-12-15', 'AESencrypted_file_20231215_144712.txt', 'AESkeystore_20231215_144712.jceks'),
(2, 1012, 'Blue', 'DES', '2023-12-15', 'DESencrypted_file_20231215_145112.txt', 'DESkeystore_20231215_145111.jceks'),
(3, 1012, 'Blue', 'CaesarCipher', '2023-12-15', 'CaesarEncrypted_file_20231215_145146.txt', 'Caesar_Key_20231215_145146.txt'),
(4, 1009, 'Blue', 'AES', '2023-12-15', 'AESencrypted_file_20231215_145805.txt', 'AESkeystore_20231215_145805.jceks');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
