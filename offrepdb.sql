-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 09, 2019 at 10:05 AM
-- Server version: 10.1.32-MariaDB
-- PHP Version: 7.2.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `offrepdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `clientstable`
--

CREATE TABLE `clientstable` (
  `clientid` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `phone_no` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pin` varchar(255) NOT NULL,
  `balance` double NOT NULL,
  `company_id` int(11) NOT NULL,
  `s` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `clientstable`
--

INSERT INTO `clientstable` (`clientid`, `name`, `address`, `city`, `phone_no`, `email`, `pin`, `balance`, `company_id`, `s`) VALUES
(1, 'Our Company', '-', '-', '-', '-', '-', 0, 0, 1),
(2, 'XYZ COMPANY', '234345-80100', 'MOMBASA', '072300010202', 'joe.tolah@gmail.com', 'XXXXXXXXXXXXX', 3088000, 1, 1),
(3, 'PWX COMPANY', '34535-80300', 'VOI', '076234234', 'agatadennis@gmail.com', 'YYYYYYYYYYYYYY', 145100, 1, 1),
(4, 'SHIGHI PRICILLAR', '32432', 'MOMBASA', '0723423423', 'shighi@gmial.com', '92343SFSD', 160000, 1, 1),
(5, 'KENYA NATIONAL UNION OF NURSES', '90184-80100', 'MOMBASA', '0727352889', 'mombasa@knun.org', '-', 0, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `clients_paytable`
--

CREATE TABLE `clients_paytable` (
  `payment_id` varchar(255) NOT NULL,
  `payment_date` varchar(255) NOT NULL,
  `client_id` int(11) NOT NULL,
  `amount` double NOT NULL,
  `s` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `company_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `clients_paytable`
--

INSERT INTO `clients_paytable` (`payment_id`, `payment_date`, `client_id`, `amount`, `s`, `user_id`, `company_id`) VALUES
('PYMT-181114082113', '2018-11-14', 2, 120000, 1, 1, 1),
('PYMT-181114083403', '2018-11-14', 2, 100000, 1, 1, 1),
('PYMT-181114084357', '2018-11-14', 2, 400000, 1, 1, 1),
('PYMT-181210182035', '2018-12-10', 2, 40000, 1, 1, 1),
('PYMT-181213105240', '2018-12-13', 4, 100000, 1, 1, 1),
('PYMT-181224132407', '2018-12-24', 5, 1000, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `companytable`
--

CREATE TABLE `companytable` (
  `id` int(11) NOT NULL,
  `company_name` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `phone_no` varchar(255) NOT NULL,
  `fax` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `email_password` varchar(50) NOT NULL,
  `pin` varchar(255) NOT NULL,
  `Dealer_in` text NOT NULL,
  `website` varchar(255) NOT NULL,
  `account_name` varchar(10) NOT NULL,
  `s` int(11) NOT NULL,
  `image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `companytable`
--

INSERT INTO `companytable` (`id`, `company_name`, `location`, `address`, `city`, `phone_no`, `fax`, `email`, `email_password`, `pin`, `Dealer_in`, `website`, `account_name`, `s`, `image`) VALUES
(1, 'Tolclin IT', 'MOI AVENUE', '84810', 'MOMBASA', '+254723095840', '-', 'josephmwawasi29@gmail.com', 'J35u5Christ', '-', 'Software Development(Desktop, WebApps and MobileApps),Websites and Consultancy', 'http://www.tolclin.com', 'TOLCLIN', 1, '/home/joe/Documents/Tolclin_IT/Designs/Exported_Docs/letterhead.png'),
(2, 'bitTEQ TECHNOLOGIES', 'MOI AVENUE', '2423', 'MOMBASA', '0723095840', '-', 'info@bitteq.com', 'swerw', '-', 'CCTV INSTALLATION', 'bitteq.com', 'TOLCLIN', 1, '/home/joe/Documents/Tolclin_IT/Designs/Exported_Docs/bitteqScreenshot.png');

-- --------------------------------------------------------

--
-- Table structure for table `expensestable`
--

CREATE TABLE `expensestable` (
  `expense_id` varchar(255) NOT NULL,
  `expense_date` date NOT NULL,
  `expense_name` varchar(255) NOT NULL,
  `mop` varchar(255) NOT NULL,
  `transactionNo` varchar(255) NOT NULL,
  `amount` double NOT NULL,
  `s` int(11) NOT NULL,
  `company_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `expensestable`
--

INSERT INTO `expensestable` (`expense_id`, `expense_date`, `expense_name`, `mop`, `transactionNo`, `amount`, `s`, `company_id`) VALUES
('EXP-190103104954', '2019-01-03', 'ELECTRICITY', 'Cash', '-', 2000, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `invoiceinfo`
--

CREATE TABLE `invoiceinfo` (
  `id` int(11) NOT NULL,
  `invoice_no` varchar(255) NOT NULL,
  `product` varchar(255) NOT NULL,
  `qty` int(11) NOT NULL,
  `unit_price` double NOT NULL,
  `price` double NOT NULL,
  `company_id` int(11) NOT NULL,
  `s` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `invoiceinfo`
--

INSERT INTO `invoiceinfo` (`id`, `invoice_no`, `product`, `qty`, `unit_price`, `price`, `company_id`, `s`) VALUES
(11, 'INV-181210114942', 'HP COMPAQ MODEL 2000', 100, 20000, 2000000, 1, 1),
(12, 'INV-181213105139', 'HP COMPAQ MODEL 2000', 20, 12000, 240000, 1, 1),
(13, 'INV-181213105139', 'HP MOUSE WIRELESS', 100, 200, 20000, 1, 1),
(14, 'INV-181224132220', 'BASIC SOFTWARES INSTALLATION', 1, 1000, 1000, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `invoicetable`
--

CREATE TABLE `invoicetable` (
  `invoice_no` varchar(255) NOT NULL,
  `client_id` int(11) NOT NULL,
  `invoice_date` date NOT NULL,
  `total` double NOT NULL,
  `discount` double NOT NULL,
  `user_id` int(11) NOT NULL,
  `company_id` int(11) NOT NULL,
  `s` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `invoicetable`
--

INSERT INTO `invoicetable` (`invoice_no`, `client_id`, `invoice_date`, `total`, `discount`, `user_id`, `company_id`, `s`) VALUES
('INV-181210114942', 2, '2018-12-10', 2000000, 0, 1, 1, 1),
('INV-181213105139', 4, '2018-12-13', 260000, 0, 1, 1, 1),
('INV-181224132220', 5, '2018-12-24', 1000, 0, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `paymentstable`
--

CREATE TABLE `paymentstable` (
  `id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `details` varchar(255) NOT NULL,
  `payment_date` date NOT NULL,
  `debit` double NOT NULL,
  `credit` double NOT NULL,
  `expenses_amount` double NOT NULL,
  `s` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `company_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `paymentstable`
--

INSERT INTO `paymentstable` (`id`, `client_id`, `details`, `payment_date`, `debit`, `credit`, `expenses_amount`, `s`, `user_id`, `company_id`) VALUES
(1, 2, 'INV-181114081934', '2018-11-14', 0, 1748000, 0, 1, 1, 1),
(2, 2, 'PYMT-181114082113', '2018-11-14', 120000, 0, 0, 1, 1, 1),
(5, 2, 'PYMT-181114083403', '2018-11-14', 100000, 0, 0, 1, 1, 1),
(7, 2, 'PYMT-181114084357', '2018-11-14', 400000, 0, 0, 1, 1, 1),
(12, 3, 'INV-181116133622', '2018-11-16', 0, 100100, 0, 1, 1, 1),
(13, 3, 'INV-181117125101', '2018-11-17', 0, 45000, 0, 1, 1, 1),
(14, 2, 'INV-181210114942', '2018-12-10', 0, 2000000, 0, 1, 1, 1),
(15, 2, 'PYMT-181210182035', '2018-12-10', 40000, 0, 0, 1, 1, 1),
(16, 4, 'INV-181213105139', '2018-12-13', 0, 260000, 0, 1, 1, 1),
(17, 4, 'PYMT-181213105240', '2018-12-13', 100000, 0, 0, 1, 1, 1),
(18, 5, 'INV-181224132220', '2018-12-24', 0, 1000, 0, 1, 1, 1),
(19, 5, 'PYMT-181224132407', '2018-12-24', 1000, 0, 0, 1, 1, 1),
(20, 1, 'EXP-190103104954', '2019-01-03', 0, 0, 2000, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `quotation_info`
--

CREATE TABLE `quotation_info` (
  `id` int(11) NOT NULL,
  `quotation_no` varchar(255) NOT NULL,
  `product` varchar(255) NOT NULL,
  `qty` int(11) NOT NULL,
  `unit_price` double NOT NULL,
  `price` double NOT NULL,
  `company_id` int(11) NOT NULL,
  `s` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `quotation_info`
--

INSERT INTO `quotation_info` (`id`, `quotation_no`, `product`, `qty`, `unit_price`, `price`, `company_id`, `s`) VALUES
(112, '1', 'HP COMPAQ MODEL 2000', 50, 20000, 1000000, 1, 1),
(113, '2', 'HP ELITEBOOK 8460P', 20, 25000, 500000, 2, 1),
(114, '2', 'HP COMPAQ MODEL 2000', 20, 20000, 400000, 2, 1),
(115, '3', 'HP COMPUTER MODEL 2000', 10, 23000, 230000, 1, 1),
(116, '4', 'SDFF', 2, 10000, 20000, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `quotation_table`
--

CREATE TABLE `quotation_table` (
  `quotation_no` int(11) NOT NULL,
  `client_name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `phone_no` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `quotation_date` date NOT NULL,
  `total` double NOT NULL,
  `user_id` int(11) NOT NULL,
  `company_id` int(11) NOT NULL,
  `s` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `quotation_table`
--

INSERT INTO `quotation_table` (`quotation_no`, `client_name`, `address`, `city`, `phone_no`, `email`, `quotation_date`, `total`, `user_id`, `company_id`, `s`) VALUES
(1, 'ABC COMPANY', '3434-80100', 'MOMBASA', '0723423423', 'info@abc.com', '2018-12-10', 1000000, 1, 1, 1),
(2, 'SHAKE COMPANY', '9345-80300', 'VOI', '0732452334', 'info@shake.com', '2018-12-10', 900000, 1, 2, 1),
(3, 'JOE TOLAH', '34523-80100', 'MOMBASA', '0723423343', 'joe.tolah@gmail.com', '2018-12-13', 230000, 1, 1, 1),
(4, 'WEQ', 'QWEW', 'WER', '342', 'rwerw', '2018-12-18', 20000, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `reminder_table`
--

CREATE TABLE `reminder_table` (
  `id` int(11) NOT NULL,
  `date` date NOT NULL,
  `details` varchar(255) NOT NULL,
  `final_date` date NOT NULL,
  `s` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `reminder_table`
--

INSERT INTO `reminder_table` (`id`, `date`, `details`, `final_date`, `s`, `user_id`) VALUES
(1, '2018-11-14', 'pay employees', '2018-11-14', 0, 1),
(2, '2018-11-14', 'pay employess', '2018-11-14', 0, 1),
(3, '2018-11-14', 'pay employee', '2018-11-14', 0, 1),
(4, '2018-11-15', 'Pay employees', '2018-11-15', 0, 1),
(5, '2018-12-04', 'pay employees', '2018-12-04', 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `renew_table`
--

CREATE TABLE `renew_table` (
  `id` int(11) NOT NULL,
  `final_date` date NOT NULL,
  `today_date` date NOT NULL,
  `code` varchar(255) NOT NULL,
  `s` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `renew_table`
--

INSERT INTO `renew_table` (`id`, `final_date`, `today_date`, `code`, `s`) VALUES
(1, '2019-07-04', '2019-01-05', '537X425', 1);

-- --------------------------------------------------------

--
-- Table structure for table `userstable`
--

CREATE TABLE `userstable` (
  `id` int(11) NOT NULL,
  `id_number` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone_no` varchar(255) NOT NULL,
  `user_type` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `last_login` varchar(255) NOT NULL,
  `company_id` int(11) NOT NULL,
  `s` int(11) NOT NULL,
  `logged` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `userstable`
--

INSERT INTO `userstable` (`id`, `id_number`, `name`, `phone_no`, `user_type`, `username`, `password`, `last_login`, `company_id`, `s`, `logged`) VALUES
(1, '001', 'JOSEPH TOLAH', '-', 'Admin', 'admin', '$2a$10$6ywWi7lIdi8n.LhhVoScaujdtOC64eqmXovRzgGd6mpXHdJ4Ma9Hq', '2019-01-05 19:39:15', 0, 1, 1),
(2, '234342342', 'JAMES BOND', '07234234322', 'User', 'user', '$2a$10$T.0wZBYypIcRxAEAPoSyUOfXiNNaD2Lzm9miXHF3pJa7owa39mZw2', '0000-00-00 00:00:00', 1, 1, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `clientstable`
--
ALTER TABLE `clientstable`
  ADD PRIMARY KEY (`clientid`);

--
-- Indexes for table `clients_paytable`
--
ALTER TABLE `clients_paytable`
  ADD PRIMARY KEY (`payment_id`);

--
-- Indexes for table `companytable`
--
ALTER TABLE `companytable`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `expensestable`
--
ALTER TABLE `expensestable`
  ADD PRIMARY KEY (`expense_id`);

--
-- Indexes for table `invoiceinfo`
--
ALTER TABLE `invoiceinfo`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `invoicetable`
--
ALTER TABLE `invoicetable`
  ADD PRIMARY KEY (`invoice_no`);

--
-- Indexes for table `paymentstable`
--
ALTER TABLE `paymentstable`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `quotation_info`
--
ALTER TABLE `quotation_info`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `quotation_table`
--
ALTER TABLE `quotation_table`
  ADD PRIMARY KEY (`quotation_no`);

--
-- Indexes for table `reminder_table`
--
ALTER TABLE `reminder_table`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `renew_table`
--
ALTER TABLE `renew_table`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `userstable`
--
ALTER TABLE `userstable`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `clientstable`
--
ALTER TABLE `clientstable`
  MODIFY `clientid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `companytable`
--
ALTER TABLE `companytable`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `invoiceinfo`
--
ALTER TABLE `invoiceinfo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `paymentstable`
--
ALTER TABLE `paymentstable`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `quotation_info`
--
ALTER TABLE `quotation_info`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=117;

--
-- AUTO_INCREMENT for table `reminder_table`
--
ALTER TABLE `reminder_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `renew_table`
--
ALTER TABLE `renew_table`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `userstable`
--
ALTER TABLE `userstable`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
